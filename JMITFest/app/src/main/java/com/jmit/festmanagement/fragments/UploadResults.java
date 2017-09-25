package com.jmit.festmanagement.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.activities.MainActivity;
import com.jmit.festmanagement.adapters.SpinnerEventAdapter;
import com.jmit.festmanagement.data.Event;
import com.jmit.festmanagement.utils.DataHandler;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arpitkh96 on 11/10/16.
 */

public class UploadResults extends BaseFragment {
    MainActivity mainActivity;
    String fest_id, fest_name;
    View rootView;
    SharedPreferences sharedPreferences;
    TextView fest_name_view;
    ArrayList<Event> eventList;
    Spinner spinnerEvent;
    SpinnerEventAdapter eventdataAdapter;
    LinearLayout itemHolder;
    String event_id;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_upload_results, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        fest_id = sharedPreferences.getString("fest_id", null);
        if (mainActivity != null) fest_name = mainActivity.getFestNameById(fest_id);
        fest_name_view = (TextView) rootView.findViewById(R.id.festLabel);
        fest_name_view.setText(fest_name);
        itemHolder = (LinearLayout) rootView.findViewById(R.id.item_holder);
        addViewInItemLayout();
        addViewInItemLayout();
        addViewInItemLayout();
        eventList = new ArrayList<>();
        if (savedInstanceState != null) {
            eventList = savedInstanceState.getParcelableArrayList("eventList");
            fest_id = savedInstanceState.getString("fest_id");
            fest_name = savedInstanceState.getString("fest_name");
        }
        spinnerEvent = (Spinner) rootView.findViewById(R.id.spinner_event);
        eventdataAdapter = new SpinnerEventAdapter(mainActivity, R.layout.spinner_row, eventList);
        spinnerEvent.setAdapter(eventdataAdapter);
        spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                event_id=eventList.get(spinnerEvent.getSelectedItemPosition()).getEventId();
                    return;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        loadEvents(fest_id);
        rootView.findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addViewInItemLayout();
                itemHolder.invalidate();

            }
        });
        rootView.findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                String s = null;
                for (int i = 0; i < itemHolder.getChildCount(); i++) {
                    View view = itemHolder.getChildAt(i);
                    EditText pos = (EditText) view.findViewById(R.id.pos);
                    EditText rollno = (EditText) view.findViewById(R.id.rollno);
                    if (pos != null && !pos.getText().toString().isEmpty() && !rollno.getText().toString().isEmpty()) {
                        if(s==null)
                            s=""+pos.getText().toString()+":"+rollno.getText().toString();
                        else     s=s+","+pos.getText().toString()+":"+rollno.getText().toString();
                    }
                }
                hashMap.put("event_id",event_id);
                hashMap.put("results",s);
                VolleyHelper.postRequestVolley(mainActivity, UploadResults.this, URL_API.UPLOAD_RESULTS, hashMap, RequestCodes.UPLOAD_RESULTS,false);
            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("eventList", eventList);
        outState.putString("fest_name", fest_name);
        outState.putString("fest_id", fest_id);
    }

    void loadEvents(String fest_id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("fest_id", fest_id);
        VolleyHelper.postRequestVolley(mainActivity, this, URL_API.EVENTS, hashMap, RequestCodes.EVENTS,true);
    }

    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);

        if (mainActivity != null)
            mainActivity.showDialog();
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);

        if (mainActivity != null)
            mainActivity.dismissDialog();
        if (requestCode == RequestCodes.EVENTS) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.getInt("success") == 1) {
                    eventList = new ArrayList<>();
                    eventList = new Gson().fromJson(jsonObject.get("events").toString(), new TypeToken<ArrayList<Event>>() {
                    }.getType());
                    String fest_id = jsonObject.getString("fest_id");
                    for (Event event : eventList) {
                        event.setFest_id(fest_id);
                        event.setRegistered(false);
                        if (DataHandler.getRegistered_events().contains(event))
                            event.setRegistered(true);
                    }
                    Event event = new Event();
                    event.setEventId("-1");
                    event.setEventName("Select Event");
                    eventList.add(0, event);
                    eventdataAdapter = new SpinnerEventAdapter(mainActivity, R.layout.spinner_row, eventList);
                    spinnerEvent.setAdapter(eventdataAdapter);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else if(requestCode==RequestCodes.UPLOAD_RESULTS){
            try {
                JSONObject jsonObject=new JSONObject(response);
                Snackbar.make(rootView,jsonObject.getString("message"),Snackbar.LENGTH_LONG).show();
                if(mainActivity!=null && jsonObject.getInt("success")==1)
                    mainActivity.fillList(-2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    void addViewInItemLayout() {
        final View view = LayoutInflater.from(mainActivity).inflate(R.layout.result_row, null);
        view.findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemHolder.removeView(view);
            }
        });
        itemHolder.addView(view);
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);

        if (mainActivity != null)
            mainActivity.dismissDialog();
    }
}
