package com.jmit.festmanagement.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;

/**
 * Created by arpitkh96 on 2/10/16.
 */

public class Event implements Parcelable  {
    @SerializedName("event_id")
    @Expose
    private String eventId;
    @SerializedName("event_name")
    @Expose
    private String eventName;
    @SerializedName("event_desc")
    @Expose
    private String eventDesc;
    @SerializedName("venue")
    @Expose
    private String venue;
    @SerializedName("start_time")
    @Expose
    private long startTime;
    @SerializedName("end_time")
    @Expose
    private long endTime;

    @SerializedName("fest_id")
    @Expose
    private String fest_id;
    transient boolean registered=false;

    protected Event(Parcel in) {
        eventId = in.readString();
        eventName = in.readString();
        eventDesc = in.readString();
        venue = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
        fest_id = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public String getFest_id() {
        return fest_id;
    }

    public void setFest_id(String fest_id) {
        this.fest_id = fest_id;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public Event() {
    }

    /**
     *
     * @return
     * The eventId
     */
    public String getEventId() {
        return eventId;
    }

    /**
     *
     * @param eventId
     * The event_id
     */
    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    /**
     *
     * @return
     * The eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     *
     * @param eventName
     * The event_name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     *
     * @return
     * The eventDesc
     */
    public String getEventDesc() {
        return eventDesc;
    }

    /**
     *
     * @param eventDesc
     * The event_desc
     */
    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    /**
     *
     * @return
     * The venue
     */
    public String getVenue() {
        return venue;
    }

    /**
     *
     * @param venue
     * The venue
     */
    public void setVenue(String venue) {
        this.venue = venue;
    }


    /**
     *
     * @return
     * The startTime
     */


    public String getStartTime() {
        SimpleDateFormat s = new SimpleDateFormat("dd MMM   HH:mm");
        return s.format(startTime);
    }
    public String getEndTime() {
        SimpleDateFormat s = new SimpleDateFormat("HH:mm");
        return s.format(startTime);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        return eventId != null ? eventId.equals(event.eventId) : event.eventId == null;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventId);
        dest.writeString(eventName);
        dest.writeString(eventDesc);
        dest.writeString(venue);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeString(fest_id);
    }
}
