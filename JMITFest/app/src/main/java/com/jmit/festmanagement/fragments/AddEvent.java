package com.jmit.festmanagement.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.VolleyError;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.activities.MainActivity;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.VolleyHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by arpitkh96 on 11/10/16.
 */

public class AddEvent extends BaseFragment {

    EditText etStartTime,etEndTime,etStartDate,etEndDate,name,venue,desc;
    String fest_id;
    String fest_name;
    TextView fest_name_view;
    MainActivity mainActivity;
    SharedPreferences sharedPreferences;
    View rootView;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("fest_name",fest_name);
        outState.putString("fest_id",fest_id);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_add_event, container, false);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(mainActivity);
        fest_id=sharedPreferences.getString("fest_id",null);

        etStartTime=(EditText)rootView.findViewById(R.id.tfStartTime);
        etEndTime=(EditText)rootView.findViewById(R.id.tfEndTime);
        etStartDate=(EditText) rootView.findViewById(R.id.tfStartDate);
        etEndDate=(EditText) rootView.findViewById(R.id.tfEndDate);
        name=(EditText)rootView.findViewById(R.id.event_name);
        desc=(EditText) rootView.findViewById(R.id.event_desc);
        venue=(EditText) rootView.findViewById(R.id.etVenue);
        if(mainActivity!=null)fest_name=mainActivity.getFestNameById(fest_id);
        fest_name_view=(TextView)rootView.findViewById(R.id.festLabel);
        fest_name_view.setText(fest_name);
        final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
        final SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
        etStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                if(!etStartTime.getText().toString().isEmpty()){
                    try {
                        Date date = null;
                        date = sdf.parse(etStartTime.getText().toString());
                        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
                        calendar.setTime(date);
                        hour=calendar.get(Calendar.HOUR);
                        minute=calendar.get(Calendar.MINUTE);
                        // assigns calendar to given date
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                    TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mainActivity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR,selectedHour>12?selectedHour-12:selectedHour+12);
                        calendar.set(Calendar.MINUTE,selectedMinute);
                        String date=sdf.format(calendar.getTime());
                        etStartTime.setText(date);

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        etEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                if(!etEndTime.getText().toString().isEmpty()){
                    try {
                        Date date = null;
                        date = sdf.parse(etEndTime.getText().toString());
                        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
                        calendar.setTime(date);
                        hour=calendar.get(Calendar.HOUR);
                        minute=calendar.get(Calendar.MINUTE);
                        // assigns calendar to given date
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(mainActivity, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Calendar calendar = Calendar.getInstance();// creates a new calendar instance
                        calendar.set(Calendar.HOUR,selectedHour>12?selectedHour-12:selectedHour+12);
                        calendar.set(Calendar.MINUTE,selectedMinute);
                        String date=sdf.format(calendar.getTime());
                        etEndTime.setText(date);
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        etStartDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                if(!etStartDate.getText().toString().isEmpty()){
                    try {
                        Date date = null;
                        date = dt1.parse(etStartDate.getText().toString());
                        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
                        calendar.setTime(date);
                        mMonth=calendar.get(Calendar.MONTH);
                        mDay=calendar.get(Calendar.DAY_OF_MONTH);
                        mYear=calendar.get(Calendar.YEAR);
                        // assigns calendar to given date
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                DatePickerDialog dpd = new DatePickerDialog(mainActivity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();// creates a new calendar instance
                                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                calendar.set(Calendar.MONTH,monthOfYear);
                                calendar.set(Calendar.YEAR,year);
                                String date=dt1.format(calendar.getTime());
                                etStartDate.setText(date);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        etEndDate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                if(!etEndDate.getText().toString().isEmpty()){
                    try {
                        Date date = null;
                        date = dt1.parse(etStartDate.getText().toString());
                        Calendar calendar = Calendar.getInstance(); // creates a new calendar instance
                        calendar.setTime(date);
                        mMonth=calendar.get(Calendar.MONTH);
                        mDay=calendar.get(Calendar.DAY_OF_MONTH);
                        mYear=calendar.get(Calendar.YEAR);
                        // assigns calendar to given date
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                DatePickerDialog dpd = new DatePickerDialog(mainActivity,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();// creates a new calendar instance
                                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                                calendar.set(Calendar.MONTH,monthOfYear);
                                calendar.set(Calendar.YEAR,year);
                                String date=dt1.format(calendar.getTime());
                                etEndDate.setText(date);

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
        rootView.findViewById(R.id.btn_add_event).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddClick();
            }
        });
        return rootView;
    }
    void onAddClick(){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("fest_id",fest_id);
        hashMap.put("event_id","-1");
        hashMap.put("event_name",name.getText().toString());
        hashMap.put("event_desc",desc.getText().toString());
        hashMap.put("venue",venue.getText().toString());
        hashMap.put("start_date",etStartDate.getText().toString());
        hashMap.put("end_date",etEndDate.getText().toString());
        hashMap.put("start_time",etStartTime.getText().toString());
        hashMap.put("end_time",etEndTime.getText().toString());
        VolleyHelper.postRequestVolley(mainActivity,this, URL_API.EDIT_EVENT,hashMap, RequestCodes.EDIT_EVENT,false);
    }

    @Override
    public void requestStarted(int requestCode) {
        super.requestStarted(requestCode);
        if(mainActivity!=null)
            mainActivity.showDialog();
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        super.requestCompleted(requestCode, response);
        if(mainActivity!=null)
            mainActivity.dismissDialog();
        try {
            JSONObject jsonObject=new JSONObject(response);
            Snackbar.make(rootView,jsonObject.getString("message"),Snackbar.LENGTH_LONG).show();
            if(mainActivity!=null && jsonObject.getInt("success")==1)
                mainActivity.fillList(-2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);
        if(mainActivity!=null)
            mainActivity.dismissDialog();
    }

    public void timePopUp(View view)
    {
        //showDialog(TIME_DIALOG_ID);
    }

}
