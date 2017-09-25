package pojo;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Event {
	String fest_id, club_name = null, event_name, event_desc, venue;
	long start_time, end_time;
	int event_id, result_available;

	public int getResult_available() {
		return result_available;
	}

	public void setResult_available(int result_available) {
		this.result_available = result_available;
	}

	public String getClub_name() {
		return club_name;
	}

	public void setClub_name(String club_name) {
		this.club_name = club_name;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public String getFest_id() {
		return fest_id;
	}

	public String getEvent_name() {
		return event_name;
	}

	public String getEvent_desc() {
		return event_desc;
	}

	public String getVenue() {
		return venue;
	}

	

	public void setFest_id(String fest_id) {
		this.fest_id = fest_id;
	}

	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}

	public void setEvent_desc(String event_desc) {
		this.event_desc = event_desc;
	}

	public void setVenue(String venue) {
		this.venue = venue;
	}

	public long getStart_time() {
		return start_time;
	}
	public String getStartTime() {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return s.format(start_time);
	}
	public String getEndTime() {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return s.format(end_time);
	}
	public void setStart_time(String start_time) {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				this.start_time = s.parse(start_time).getTime();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	public long getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			this.start_time = s.parse(end_time).getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}


}
