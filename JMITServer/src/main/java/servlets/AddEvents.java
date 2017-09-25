package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import utils.Constants;
import database.CacheConnection;
import database.DataManager;
import pojo.Event;


/**
 * Servlet implementation class create_user
 */
@WebServlet("/add_events")
public class AddEvents extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddEvents() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		String fest_id = null,event_name = null,event_desc=null,venue=null,start_time=null,end_time=null,club_name=null;
		Map m = request.getParameterMap();
		Set s = m.entrySet();
		Iterator it = s.iterator();

		while (it.hasNext()) {

			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();

			String key = entry.getKey();
			String[] value = entry.getValue();
			switch (key) {
			case Constants.event_name:
				event_name = value[0].toString();
				break;
			case Constants.event_desc:
				event_desc = value[0].toString();
				break;
			case Constants.venue:
				venue = value[0].toString();
				break;
			case Constants.start_time:
		     	start_time = value[0].toString();
				break;
			case Constants.end_time:
			     	end_time = value[0].toString();
					break;		
			case Constants.club_name:
		     	club_name = value[0].toString();
				break;
			}

		}
		CacheConnection.setVerbose(true);

		// Get a cached connection
		java.sql.Connection connection = CacheConnection.checkOut();
		try {
			connection.setReadOnly(false);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		boolean b = false;
		try {
			Event data=new  Event();
			data.setFest_id("-1");
			data.setEvent_name(event_name);
			data.setEvent_desc(event_desc);
			data.setVenue(venue);
			data.setStart_time(start_time);
			data.setEnd_time(end_time);
			data.setClub_name(club_name);
			
			
			b = DataManager.addevents(connection, data);
			if (b) {
				JsonObject json = new JsonObject();
				json.addProperty("success", "1");
				json.addProperty("message", "event successfully added");
				response.getWriter().write(json.toString());
			} else if (!b) {
				JsonObject json = new JsonObject();
				json.addProperty("success", "0");
				json.addProperty("message", "Oops an error occured");
				response.getWriter().write(json.toString());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CacheConnection.checkIn(connection);

	}

	
	}

