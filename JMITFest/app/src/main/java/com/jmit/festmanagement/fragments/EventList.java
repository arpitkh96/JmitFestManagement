package com.jmit.festmanagement.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jmit.festmanagement.R;
import com.jmit.festmanagement.activities.MainActivity;
import com.jmit.festmanagement.adapters.EventAdapter;
import com.jmit.festmanagement.data.Event;
import com.jmit.festmanagement.utils.DataHandler;
import com.jmit.festmanagement.utils.EmptyRecyclerView;
import com.jmit.festmanagement.utils.FLog;
import com.jmit.festmanagement.utils.RequestCodes;
import com.jmit.festmanagement.utils.URL_API;
import com.jmit.festmanagement.utils.VolleyHelper;
import com.jmit.festmanagement.utils.VolleyInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by arpitkh96 on 10/10/16.
 */

public class EventList extends BaseFragment implements EventAdapter.OnItemClickListener, DataHandler.RegisteredEventsListener {
    ArrayList<Event> currentEventList;
    EmptyRecyclerView eventRecycler;
    EventAdapter eventAdapter;
    String uid, user_id;
    MainActivity mainActivity;
    int mode;
    String fest_id;
    SwipeRefreshLayout swipeRefreshLayout;

    public static EventList newInstance(int mode, String fest_id) {
        Bundle args = new Bundle();
        args.putInt("mode", mode);
        args.putString("fest_id", fest_id);
        EventList fragment = new EventList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mainActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mainActivity);
        uid = sharedPreferences.getString("uid", null);
        user_id = sharedPreferences.getString("user_id", null);
        currentEventList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mainActivity.getLayoutInflater().inflate(R.layout.event_list, null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("list", currentEventList);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialiseList(view);
        fest_id = getArguments().getString("fest_id");
        mode = getArguments().getInt("mode");
        if(mode==-2)
        ((TextView)view.findViewById(R.id.nodata_textview1)).setText("No Registered Events");
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        view.findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        DataHandler.setRegisteredEventsListener(this);
        if (savedInstanceState == null) {
            refresh();
        } else {
            currentEventList = savedInstanceState.getParcelableArrayList("list");
            initialiseList(view);
        }
    }

    void refresh() {
        if (fest_id != null) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("fest_id", fest_id);
            VolleyHelper.postRequestVolley(mainActivity, this, URL_API.EVENTS, hashMap, RequestCodes.EVENTS, true);
        } else if (mode == -2) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("user_id", user_id);
            VolleyHelper.postRequestVolley(mainActivity, this, URL_API.REGISTERED_EVENTS, hashMap, RequestCodes.GET_REGISTRATION, false);
        } else if (mode == -4) {
            HashMap<String, String> hashMap = new HashMap<>();
            VolleyHelper.postRequestVolley(mainActivity, this, URL_API.CLUB_EVENTS, hashMap, RequestCodes.CLUB_EVENTS, false);
        }
    }

    @Override
    public void requestStarted(int requestCode) {
        if (requestCode == RequestCodes.EVENTS || requestCode == RequestCodes.GET_REGISTRATION || requestCode == RequestCodes.CLUB_EVENTS) {
            if (swipeRefreshLayout.isRefreshing()) {

            } else {
                eventRecycler.setTaskRunning(true);
            }
        } else if (requestCode == RequestCodes.REGISTRATION || requestCode == RequestCodes.UNREGISTRATION) {
            if (mainActivity != null)
                mainActivity.showDialog();
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
                    currentEventList = new ArrayList<>();
                    currentEventList = new Gson().fromJson(jsonObject.get("events").toString(), new TypeToken<ArrayList<Event>>() {
                    }.getType());
                    String fest_id = jsonObject.getString("fest_id");
                    for (Event event : currentEventList) {
                        event.setFest_id(fest_id);
                        event.setRegistered(false);
                        if (DataHandler.getRegistered_events().contains(event))
                            event.setRegistered(true);
                    }
                    eventAdapter.setMyeventsMode(false);
                    eventAdapter.setHeaderList(currentEventList);
                    eventAdapter.notifyDataSetChanged();
                    eventRecycler.checkIfEmpty();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RequestCodes.GET_REGISTRATION) {
            JSONObject jsonObject = null;
            currentEventList = new ArrayList<>();
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.getInt("success") == 1)
                    currentEventList = new Gson().fromJson(jsonObject.get("registration").toString(), new TypeToken<ArrayList<Event>>() {
                    }.getType());

                if (currentEventList == null)
                    currentEventList = new ArrayList<>();
                for (Event e : currentEventList) e.setRegistered(true);
                DataHandler.setRegistered_events(currentEventList, false);
                eventAdapter.setMyeventsMode(true);
                eventAdapter.setHeaderList(currentEventList);
                eventAdapter.notifyDataSetChanged();
                eventRecycler.checkIfEmpty();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RequestCodes.CLUB_EVENTS) {
            JSONObject jsonObject = null;
            currentEventList = new ArrayList<>();
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.getInt("success") == 1)
                    currentEventList = new ArrayList<>();
                currentEventList = new Gson().fromJson(jsonObject.get("events").toString(), new TypeToken<ArrayList<Event>>() {
                }.getType());
                for (Event event : currentEventList) {
                    event.setRegistered(false);
                    if (DataHandler.getRegistered_events().contains(event))
                        event.setRegistered(true);
                }
                eventAdapter.setMyeventsMode(false);
                eventAdapter.setHeaderList(currentEventList);
                eventAdapter.notifyDataSetChanged();
                eventRecycler.checkIfEmpty();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RequestCodes.UNREGISTRATION) {
            if (mainActivity != null)
                mainActivity.dismissDialog();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.getInt("success") == 1) {
                    refresh();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (requestCode == RequestCodes.REGISTRATION) {
            if (mainActivity != null)
                mainActivity.dismissDialog();
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(response);
                if (jsonObject.getInt("success") == 1) {
                    String event_id = jsonObject.getString("event_id");
                    if ( event_id != null) {
                        Event event = new Event();
                        event.setEventId(event_id);
                        DataHandler.addEvent(event, true);

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == RequestCodes.EVENTS || requestCode == RequestCodes.GET_REGISTRATION || requestCode == RequestCodes.CLUB_EVENTS) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            } else {
                eventRecycler.setTaskRunning(false);
            }
        }
    }

    @Override
    public void requestEndedWithError(int requestCode, VolleyError error) {
        super.requestEndedWithError(requestCode, error);
        if (requestCode == RequestCodes.EVENTS || requestCode == RequestCodes.GET_REGISTRATION  || requestCode == RequestCodes.CLUB_EVENTS) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            } else {
                eventRecycler.setTaskRunning(false);
            }
        } else if (requestCode == RequestCodes.REGISTRATION || requestCode == RequestCodes.UNREGISTRATION) {
            if (mainActivity != null)
                mainActivity.dismissDialog();
        }
    }

    void initialiseList(View view) {
        eventRecycler = (EmptyRecyclerView) view.findViewById(R.id.eventRecycler);
        eventRecycler.setEmptyView((ContentLoadingProgressBar) view.findViewById(R.id.event_progressBar), view.findViewById(R.id.event_nodata));
        LinearLayoutManager manager1 = new LinearLayoutManager(mainActivity);
        eventRecycler.setLayoutManager(manager1);
        eventAdapter = new EventAdapter(currentEventList, eventRecycler, mainActivity, (mode == -2 || mode==-4) ? true : false);
        eventAdapter.setOnItemClickListener(this);
        eventRecycler.setAdapter(eventAdapter);
        eventRecycler.checkIfEmpty();

    }

    @Override
    public void onEventItemClick(Event event) {
        if (mainActivity != null)
            mainActivity.showBottomSheet(event);
    }

    @Override
    public void onRegisterClick(Event event, boolean register) {
        if (register) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("event_id", event.getEventId());
            hashMap.put("user_id", user_id);
            VolleyHelper.postRequestVolley(mainActivity, this, URL_API.REGISTRATION, hashMap, RequestCodes.REGISTRATION, false);
        } else {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("event_id", event.getEventId());
            hashMap.put("user_id", user_id);
            VolleyHelper.postRequestVolley(mainActivity, this, URL_API.UNREGISTRATION, hashMap, RequestCodes.UNREGISTRATION, false);
        }
    }

    @Override
    public void onRegisteredEventsChanged(ArrayList<Event> events) {
        for (Event event : currentEventList) {
            if (DataHandler.getRegistered_events().contains(event))
                event.setRegistered(true);
        }

        eventAdapter.setHeaderList(currentEventList);
        eventAdapter.notifyDataSetChanged();
        eventRecycler.checkIfEmpty();
    }
}
