package com.jmit.festmanagement.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.activities.MainActivity;
import com.jmit.festmanagement.adapters.RegistrationAdapter;
import com.jmit.festmanagement.adapters.SpinnerEventAdapter;
import com.jmit.festmanagement.data.Event;
import com.jmit.festmanagement.data.Fest;
import com.jmit.festmanagement.data.Registration;
import com.jmit.festmanagement.utils.DataHandler;
import com.jmit.festmanagement.utils.EmptyRecyclerView;
import com.jmit.festmanagement.utils.FLog;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.VolleyHelper;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arpitkh96 on 11/10/16.
 */

public class ViewRegistrations extends BaseFragment {
    EmptyRecyclerView emptyRecyclerView;
    ArrayList<Event> eventList;
    ArrayList<Registration> registrations;
    MainActivity mainActivity;
    ArrayAdapter eventdataAdapter;
    SharedPreferences sharedPreferences;
    Spinner spinnerEvent;
    RegistrationAdapter registrationAdapter;
    String fest_id;
    String fest_name;
    TextView fest_name_view;
    View rootView;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_view_registrations, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        fest_id = sharedPreferences.getString("fest_id", null);
        return rootView;
    }

    @Override
    public void onViewCreated(View rootView, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        spinnerEvent = (Spinner) rootView.findViewById(R.id.spinner_event);
        registrations = new ArrayList<>();
        eventList = new ArrayList<>();
        if (savedInstanceState != null) {
            eventList = savedInstanceState.getParcelableArrayList("eventList");
            registrations = savedInstanceState.getParcelableArrayList("registrations");
            fest_id = savedInstanceState.getString("fest_id");
            fest_name = savedInstanceState.getString("fest_name");
        }
        initialiseList(rootView);
        loadEvents(fest_id);
        rootView.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadEvents(fest_id);
            }
        });

        if (mainActivity != null) fest_name = mainActivity.getFestNameById(fest_id);
        fest_name_view = (TextView) rootView.findViewById(R.id.festLabel);
        fest_name_view.setText(fest_name);
        eventdataAdapter = new SpinnerEventAdapter(mainActivity, R.layout.spinner_row, eventList);
        spinnerEvent.setAdapter(eventdataAdapter);
        spinnerEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!eventList.get(spinnerEvent.getSelectedItemPosition()).getEventId().equals("-1"))
                    loadRegistrations(fest_id, eventList.get(spinnerEvent.getSelectedItemPosition()).getEventId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("registrations", registrations);
        outState.putParcelableArrayList("eventList", eventList);
        outState.putString("fest_name", fest_name);
        outState.putString("fest_id", fest_id);
    }

    void loadEvents(String fest_id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("fest_id", fest_id);
        VolleyHelper.postRequestVolley(getActivity(), this, URL_API.EVENTS, hashMap, RequestCodes.EVENTS, true);
    }

    void loadRegistrations(String fest_id, String event_id) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("fest_id", fest_id);
        hashMap.put("event_id", event_id);
        VolleyHelper.postRequestVolley(getActivity(), this, URL_API.ADMIN_REGISTERED_EVENTS, hashMap, RequestCodes.ADMIN_REGISTERED_EVENTS, true);
    }

    void initialiseList(View view) {
        emptyRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.eventRecycler);
        emptyRecyclerView.setEmptyView((ContentLoadingProgressBar) view.findViewById(R.id.event_progressBar), view.findViewById(R.id.event_nodata));
        LinearLayoutManager manager1 = new LinearLayoutManager(mainActivity) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        emptyRecyclerView.setLayoutManager(manager1);
        registrationAdapter = new RegistrationAdapter(registrations, mainActivity);
        emptyRecyclerView.setAdapter(registrationAdapter);
    }
    public void export(){
        if(registrations==null || registrations.size()==0){
            Snackbar.make(rootView,"Nothing to export",Snackbar.LENGTH_SHORT).show();
            return;
        }
        export(registrations);
    }
    void export(ArrayList<Registration> arrayList) {
        HSSFWorkbook workbook = new HSSFWorkbook();

        HSSFSheet firstSheet = workbook.createSheet("Entries");

        HSSFRow rowA = firstSheet.createRow(0);
        HSSFCell cellA = rowA.createCell(0);
        cellA.setCellValue(new HSSFRichTextString("Roll no"));
        HSSFCell cell = rowA.createCell(1);
        cell.setCellValue(new HSSFRichTextString("Name"));
        HSSFCell cell1 = rowA.createCell(2);
        cell1.setCellValue(new HSSFRichTextString("Phone no"));
        HSSFCell cell2 = rowA.createCell(3);
        cell2.setCellValue(new HSSFRichTextString("Email"));
        HSSFCell cell3 = rowA.createCell(4);
        cell3.setCellValue(new HSSFRichTextString("Dept"));
        int i = 1;
        for (Registration registration : arrayList) {
            HSSFRow row = firstSheet.createRow(i);
            row.createCell(0).setCellValue(registration.getRollNo());
            row.createCell(1).setCellValue(registration.getUserName());
            row.createCell(2).setCellValue(registration.getPhNo());
            row.createCell(3).setCellValue(registration.getEmail());
            row.createCell(4).setCellValue(registration.getDepartment());
            i++;
        }
        FileOutputStream fos = null;
        File file=null;
        try {

            String str_path = mainActivity.getExternalFilesDir(null).getPath();

            file = new File(str_path, getString(R.string.app_name) + ".xls");
            fos = new FileOutputStream(file);


            workbook.write(fos);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            Toast.makeText(mainActivity, "Excel Sheet Generated", Toast.LENGTH_SHORT).show();
            Uri uri=Uri.fromFile(file);

            final Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.ms-excel");
            mainActivity.startActivity(intent);
        }
    }

    @Override
    public void requestStarted(int requestCode) {
        if (requestCode == RequestCodes.ADMIN_REGISTERED_EVENTS || requestCode == RequestCodes.EVENTS) {
            emptyRecyclerView.setTaskRunning(true);
        }
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);

        if (requestCode == RequestCodes.ADMIN_REGISTERED_EVENTS || requestCode == RequestCodes.EVENTS) {
            emptyRecyclerView.setTaskRunning(false);
        }
    }

    @Override
    public void requestCompleted(int requestCode, String response) {
        FLog.d(response);

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
                    event.setEventName("Select Event");
                    event.setEventId("-1");
                    eventList.add(0, event);
                    eventdataAdapter = new SpinnerEventAdapter(mainActivity, R.layout.spinner_row, eventList);
                    spinnerEvent.setAdapter(eventdataAdapter);
                    emptyRecyclerView.setTaskRunning(false);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RequestCodes.ADMIN_REGISTERED_EVENTS) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                registrations = new ArrayList<>();
                if (jsonObject.getInt("success") == 1) {
                    registrations = new Gson().fromJson(jsonObject.get("registrations").toString(), new TypeToken<ArrayList<Registration>>() {
                    }.getType());
                }

                registrationAdapter.setHeaderList(registrations);
                registrationAdapter.notifyDataSetChanged();
                emptyRecyclerView.setTaskRunning(false);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
