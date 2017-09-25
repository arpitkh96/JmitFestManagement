package com.jmit.festmanagement.activities;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.adapters.DrawerAdapter;
import com.jmit.festmanagement.data.Event;
import com.jmit.festmanagement.data.Fest;
import com.jmit.festmanagement.fragments.AddEvent;
import com.jmit.festmanagement.fragments.EditEvent;
import com.jmit.festmanagement.fragments.EventDialog;
import com.jmit.festmanagement.fragments.EventList;
import com.jmit.festmanagement.fragments.UploadResults;
import com.jmit.festmanagement.fragments.ViewRegistrations;
import com.jmit.festmanagement.utils.DataHandler;
import com.jmit.festmanagement.utils.EmptyRecyclerView;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends BaseActivity implements View.OnClickListener, DrawerAdapter.OnItemClickListener {

    private EmptyRecyclerView drawerRecycler;
    private ArrayList<Fest> headerList;
    private DrawerAdapter customAdapter;
    DrawerLayout drawer;
    String title;
    String uid;
    int mode = -2;
    boolean isAdmin = false;
    SharedPreferences sharedPreferences;
    String user, email, phone;

    /*
    *-3 for home (not present yet)
    * -2 for MyEvents
    * 0,1,2...for fests
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intialiseViews();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        uid = sharedPreferences.getString("uid", null);
        fillDrawerHeader();
        headerList = new ArrayList<>();
        title = getResources().getString(R.string.app_name);
        if (mode == -2)
            title = "My Events";
        else if (mode == -3)
            title = "Home";
        if (savedInstanceState == null) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("user_id", uid);
            VolleyHelper.postRequestVolley(this, URL_API.REGISTERED_EVENTS, hashMap, RequestCodes.GET_REGISTRATION,false);
            VolleyHelper.postRequestVolley(this, URL_API.FESTS, new HashMap<String, String>(), RequestCodes.FESTS,true);
            fillList(-2);
        } else {
            headerList = savedInstanceState.getParcelableArrayList("headerList");
            title = savedInstanceState.getString("title");
            mode = savedInstanceState.getInt("mode");
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("fest_id", "2");
        hashMap.put("event_id", "1");
        getSupportActionBar().setTitle(title);
        initialiseLists(savedInstanceState != null);
    }

    void fillDrawerHeader() {
        user = sharedPreferences.getString("user", "");
        email = sharedPreferences.getString("email", "");
        phone = sharedPreferences.getString("phone", "");
        isAdmin = false;//sharedPreferences.getBoolean("isAdmin", false);

        TextView textView = (TextView) findViewById(R.id.username);
        textView.setText(user + "(" + uid + ")");
        TextView email1 = (TextView) findViewById(R.id.email);
        email1.setText(email);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(mode!=-2){
                fillList(-2);
            }
            else
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("headerList", headerList);
        outState.putInt("mode", mode);
        outState.putString("title", title);
    }

    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);

        if (requestCode == RequestCodes.FESTS) {
            drawerRecycler.setTaskRunning(true);
        } else if (requestCode == RequestCodes.EDIT_DETAILS) {
            showDialog();
        }
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);
        if (requestCode == RequestCodes.FESTS) {
            drawerRecycler.setTaskRunning(false);
        } else if (requestCode == RequestCodes.EDIT_DETAILS) {
            dismissDialog();
        }
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        if (requestCode == RequestCodes.FESTS) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                headerList = new Gson().fromJson(jsonObject.get("fests").toString(), new TypeToken<ArrayList<Fest>>() {
                }.getType());
                customAdapter.setHeaderList(headerList);
                customAdapter.notifyDataSetChanged();
                drawerRecycler.checkIfEmpty();
                if(isAdmin)
                    findViewById(R.id.adminPanel).setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RequestCodes.GET_REGISTRATION) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                int i = jsonObject.getInt("success");
                if (i == 1) {
                    ArrayList<Event> arrayList = new Gson().fromJson(jsonObject.get("registrations").toString(), new TypeToken<ArrayList<Event>>() {
                    }.getType());
                    DataHandler.setRegistered_events(arrayList, true);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RequestCodes.EDIT_DETAILS) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                int i = jsonObject.getInt("success");
                if (i == 1) {
                    SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                    editor.putString("user", jsonObject.getString("user_name")).commit();
                    editor.putString("email", jsonObject.getString("email")).commit();
                    editor.putString("phone", jsonObject.getString("ph_no")).commit();
                    fillDrawerHeader();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == RequestCodes.FESTS) {
            drawerRecycler.setTaskRunning(false);
        } else if (requestCode == RequestCodes.EDIT_DETAILS) {
            dismissDialog();
        }
    }
/*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onDrawerItemClick(int item) {
        mode = item;
        title = headerList.get(item).getFestName();
        getSupportActionBar().setTitle(title);
        addEventsFragment(0, headerList.get(item).getFestId());
        if (drawer != null)
            drawer.closeDrawer(GravityCompat.START);
    }

    void intialiseViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        drawerRecycler = (EmptyRecyclerView) findViewById(R.id.recyclerview);
        drawerRecycler.setEmptyView((ContentLoadingProgressBar) findViewById(R.id.progressBar), findViewById(R.id.nodata));
        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit().putString("uid", null).commit();
                startActivity(new Intent(MainActivity.this, SplashActivity.class));
                finish();
            }
        });
    }

    void initialiseLists(boolean rotate) {
        LinearLayoutManager manager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        drawerRecycler.setLayoutManager(manager);
        customAdapter = new DrawerAdapter(headerList, getApplicationContext());
        customAdapter.setOnItemClickListener(this);
        drawerRecycler.setAdapter(customAdapter);
        drawerRecycler.checkIfEmpty();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.activity_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(mode==-7){
            menu.findItem(R.id.export).setVisible(true);
        }
        else menu.findItem(R.id.export).setVisible(false);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.export:
                Fragment fragment=getSupportFragmentManager().findFragmentById(R.id.main_frame);
                if(fragment instanceof ViewRegistrations){
                    ViewRegistrations registrations=(ViewRegistrations)fragment;
                    registrations.export();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void fillList(int mode) {

        if (mode == -2 || mode == -4) {

            title = mode == -2 ?"My Events":"Club Events";
            getSupportActionBar().setTitle(title);
            addEventsFragment(mode, null);

        }
        this.mode = mode;
    }

    void addEventsFragment(int mode, String fest_id) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.main_frame, EventList.newInstance(mode, fest_id));
        transaction.commit();
        supportInvalidateOptionsMenu();
    }

    void addFragment(Fragment fragment) {
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
        transaction.replace(R.id.main_frame, fragment);
        transaction.commit();
        supportInvalidateOptionsMenu();
    }

    public void editDetails(View v) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_edit_details, null);
        final TextInputEditText name1 = (TextInputEditText) view.findViewById(R.id.name);
        final TextInputEditText phone1 = (TextInputEditText) view.findViewById(R.id.phone);
        final TextInputEditText email1 = (TextInputEditText) view.findViewById(R.id.email);
        name1.setText(this.user);
        phone1.setText(this.phone);
        email1.setText(this.email);
        builder.customView(view, true);
        builder.title("Edit Details");
        builder.negativeText("Cancel");
        builder.positiveText("Set");
        builder.onPositive(new MaterialDialog.SingleButtonCallback() {
            @Override
            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("roll_no", uid);
                hashMap.put("user_name", name1.getText().toString());
                hashMap.put("ph_no", phone1.getText().toString());
                hashMap.put("email", email1.getText().toString());
                VolleyHelper.postRequestVolley(MainActivity.this, URL_API.EDIT_DETAILS, hashMap, RequestCodes.EDIT_DETAILS,false);
            }
        });
        builder.show();
    }
    int i=1;
    final static String GROUP_KEY_EMAILS = "group_key_emails";
    NotificationManagerCompat notificationManager ;
    public void myEvents(View v) {
        fillList(-2);
        drawer.closeDrawer(GravityCompat.START);
    }
    public void clubevents(View v) {
        fillList(-4);
        drawer.closeDrawer(GravityCompat.START);
    }
    public void home(View v) {
        fillList(-3);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void adminPanel(View v) {
        Fragment fragment=null;
        switch (v.getId()) {
            case R.id.addEvent:
                fragment = new AddEvent();
                title = "Add Event";
                mode=-5;
                break;
            case R.id.editevent:
                fragment = new EditEvent();
                title = "Edit Event";
                mode=-6;
                break;
            case R.id.get_registrations:
                fragment = new ViewRegistrations();
                title = "View Entries";
                mode=-7;
                break;
            case R.id.upload_results:
                fragment = new UploadResults();
                title = "Upload Results";
                mode=-8;
                break;
        }
        addFragment(fragment);
        getSupportActionBar().setTitle(title);

        drawer.closeDrawer(GravityCompat.START);
    }

    public ArrayList<Fest> getHeaderList() {
        return headerList;
    }
    public void showBottomSheet(Event drawerItem){
        BottomSheetDialogFragment bottomSheetDialogFragment = EventDialog.newInstance(drawerItem,getBottmSheetPeekHeight());
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }
    public int getBottmSheetPeekHeight(){
        int actionBarHeight=0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }
    public String getFestNameById(String fest_id){
        for(Fest fest:headerList){
            if(fest.getFestId().equals(fest_id))
                return fest.getFestName();
        }
        return "";
    }
}
