package servlets;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
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
import pojo.Logininfo;
import pojo.Registration;

/**
 * Servlet implementation class Getregistration
 */
@WebServlet("/get_registeration")
public class Getregistration extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Getregistration() {
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
		String event_id = null;
		Map m = request.getParameterMap();
		Set s = m.entrySet();
		Iterator it = s.iterator();

		while (it.hasNext()) {

			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();

			String key = entry.getKey();
			String[] value = entry.getValue();
			switch (key) {
			
			case Constants.event_id:
				event_id = value[0].toString();
				break;
			
			}

		}
		CacheConnection.setVerbose(true);

		// Get a cached connection
		java.sql.Connection connection = CacheConnection.checkOut();
		try {
			connection.setReadOnly(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList<Registration>list = null;
		try {
			
			list=DataManager.getregistration(connection, event_id);
			if (list!=null && list.size()>0) {
				JsonObject json = new JsonObject();
				json.addProperty("success", "1");
		     	json.addProperty("registrations", gson.toJson(list));
				response.getWriter().write(json.toString());
			} else  {
				JsonObject json = new JsonObject();
				json.addProperty("success", "0");
				json.addProperty("message", "No registration found");
				response.getWriter().write(json.toString());

			}
		} catch (Exception e) {

		//	response.getWriter().write(Utils.getErrorMessage());

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CacheConnection.checkIn(connection);


	}

	}
