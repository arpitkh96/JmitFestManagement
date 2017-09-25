package servlets;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import utils.Constants;
import utils.Logger;
import database.CacheConnection;
import database.DataManager;
import pojo.Logininfo;


/**
 * Servlet implementation class user_login
 */
@WebServlet("/user_login")
public class user_login extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public user_login() {
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
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		StringBuilder sb = new StringBuilder();
		String password = null,rollno = null,deviceid=null;
		Map m = request.getParameterMap();
		Set s = m.entrySet();
		Iterator it = s.iterator();

		while (it.hasNext()) {

			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();

			String key = entry.getKey();
			String[] value = entry.getValue();
			switch (key) {
			case Constants.roll_no:
				rollno = value[0].toString();
				break;

			case Constants.password:
				password = value[0].toString();
				break;
			case Constants.device_id:
				deviceid = value[0].toString();
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
			b = DataManager.login(connection,rollno,password,deviceid );
			if (b) {
				Logininfo list=new DataManager().getRecord(connection,rollno);
				if(deviceid.equals("web")){
					Cookie loginCookie = new Cookie("user",rollno);
					Cookie eventCookie = new Cookie("event",list.getFest_id());
					//setting cookie to expiry in 30 mins
					loginCookie.setMaxAge(30*60);
					eventCookie.setMaxAge(30*60);
					response.addCookie(loginCookie);
					response.addCookie(eventCookie);
					response.sendRedirect("entries");
					return;
				}
				JsonObject json = new JsonObject();
				json.addProperty("success", "1");
				json.addProperty("message", "You are logged in");
				json.addProperty("user_id", list.getUserid());
				json.addProperty("user_name", list.getUsername());
				json.addProperty("email", list.getEmail());
				json.addProperty("ph_no", list.getPhno());
				json.addProperty("isadmin", list.getIsadmin());
				json.addProperty("fest_id", list.getFest_id());
				response.getWriter().write(json.toString());
			} else if (!b) {
				if(deviceid.equals("web")){
					Cookie loginCookie = new Cookie("message","Invalid username or password");
					//setting cookie to expiry in 30 mins
					loginCookie.setMaxAge(1*60);
					response.addCookie(loginCookie);
					response.sendRedirect("login");
					return;
				}
				JsonObject json = new JsonObject();
				json.addProperty("success", "0");
				json.addProperty("message", "Invalid username or password");
				response.getWriter().write(json.toString());

			}
		} catch (Exception e) {

		//	response.getWriter().write(Utils.getErrorMessage());

			JsonObject json = new JsonObject();
			json.addProperty("success", "0");
			json.addProperty("message", e.getMessage());
			response.getWriter().write(json.toString());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		CacheConnection.checkIn(connection);

	}

}
