package com.jmit.festmanagement.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.Utils;
import com.jmit.festmanagement.utils.VolleyHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

public class SignUpActivity extends BaseActivity {

    EditText rollno, name, phoneno, email, cemail,pass;
    Button signup;
    private String[] arraySpinner;
    AppCompatSpinner department;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rollno = (EditText) findViewById(R.id.roll);
        name = (EditText) findViewById(R.id.fName);
        department = (AppCompatSpinner) findViewById(R.id.department);
        phoneno = (EditText) findViewById(R.id.phone);
        email = (EditText) findViewById(R.id.Email);
        cemail = (EditText) findViewById(R.id.cemail);
        signup=(Button)findViewById(R.id.btn_signup);
        pass=(EditText)findViewById(R.id.pass);
        this.arraySpinner = new String[] {
                "Select Department", "CSE", "ME", "ECE", "MCA","EEE","MBA","IT","CHE"
        };
        MyCustomAdapter adapter = new MyCustomAdapter(this,
                R.layout.spinner_row, arraySpinner);
        department.setAdapter(adapter);
        department.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allvalid=true;
                if (rollno.getText().toString().matches("") || name.getText().toString().matches("") || department.getSelectedItemPosition()==0 || phoneno.getText().toString().matches("") || email.getText().toString().matches("") || cemail.getText().toString().matches("") || pass.getText().toString().matches("")) {
                    allvalid=false;
                    Toast.makeText(getApplicationContext(), "Fill All The Blanks", Toast.LENGTH_SHORT).show();
                }
                if (!isValidEmail(email.getText())){
                    allvalid=false;
                    Toast.makeText(getApplicationContext(), "Check your email", Toast.LENGTH_SHORT).show();
                }
                if(phoneno.getText().toString().length() < 7 || phoneno.getText().toString().length() > 13) {
                    allvalid=false;
                    Toast.makeText(getApplicationContext(), "Check your Phone Number", Toast.LENGTH_SHORT).show();
                }
                if (!email.getText().toString().equals(cemail.getText().toString())){
                    allvalid=false;
                    Toast.makeText(getApplicationContext(), "Email not matched", Toast.LENGTH_SHORT).show();
                }
                if (allvalid){
                    HashMap<String,String> hashMap=new HashMap<String, String>();
                    hashMap.put("roll_no",rollno.getText().toString());
                    hashMap.put("user_name",name.getText().toString());
                    hashMap.put("department",arraySpinner[department.getSelectedItemPosition()]);
                    hashMap.put("ph_no",phoneno.getText().toString());
                    hashMap.put("email",email.getText().toString());
                    hashMap.put("password",pass.getText().toString());
                    VolleyHelper.postRequestVolley(SignUpActivity.this, URL_API.SIGN_UP,hashMap, RequestCodes.SIGN_UP,false);
                }
            }
        });

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(this,StudentLogin.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(StudentLogin.class);

    }

    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);
        showDialog();
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);
        dismissDialog();
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        dismissDialog();
        try {
            JSONObject jsonObject=new JSONObject(response);
            int i=jsonObject.getInt("success");
            if(i==1){
                Toast.makeText(this,jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                startActivity(StudentLogin.class);
            }
            else Snackbar.make(findViewById(R.id.main_frame),jsonObject.getString("message"),Snackbar.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyCustomAdapter extends ArrayAdapter<String>{

        public MyCustomAdapter(Context context, int textViewResourceId,
                               String[] objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, false, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, true, parent);
        }

        public View getCustomView(int position,boolean white, ViewGroup parent) {

            LayoutInflater inflater=getLayoutInflater();
            View row=inflater.inflate(R.layout.spinner_row, parent, false);
            TextView label=(TextView)row.findViewById(android.R.id.text1);
            if(white)
                label.setTextColor(Color.WHITE);
            else label.setTextColor(Color.BLACK);
            label.setText(getItem(position));

            return row;
        }
    }

}
