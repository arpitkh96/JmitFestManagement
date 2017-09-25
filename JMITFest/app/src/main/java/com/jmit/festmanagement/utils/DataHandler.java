package com.jmit.festmanagement.utils;

import com.jmit.festmanagement.data.Event;

import java.util.ArrayList;

/**
 * Created by arpitkh96 on 10/10/16.
 */

public class DataHandler {
    static ArrayList<Event> registered_events;
    static RegisteredEventsListener registeredEventsListener;
    static {
        registered_events=new ArrayList<>();
    }

    public static ArrayList<Event> getRegistered_events() {
        return registered_events;
    }

    public static void setRegistered_events(ArrayList<Event> registered_events,boolean callInterface) {
        DataHandler.registered_events = registered_events;
        if(registeredEventsListener!=null && callInterface)
            registeredEventsListener.onRegisteredEventsChanged(registered_events);
    }

    public static void setRegisteredEventsListener(RegisteredEventsListener registeredEventsListener1) {
        registeredEventsListener = registeredEventsListener1;
    }
    public static void addEvent(Event event,boolean callInterface){
        registered_events.add(event);
        if(registeredEventsListener!=null && callInterface)
            registeredEventsListener.onRegisteredEventsChanged(registered_events);

    }

    public interface RegisteredEventsListener{
    void onRegisteredEventsChanged(ArrayList<Event> events);
    }
}
