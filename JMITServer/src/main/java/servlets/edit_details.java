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

/**
 * Servlet implementation class edit_details
 */
@WebServlet("/edit_details")
public class edit_details extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public edit_details() {
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
		String user_id = null,user_name=null,ph_no=null,email=null;
		Map m = request.getParameterMap();
		Set s = m.entrySet();
		Iterator it = s.iterator();

		while (it.hasNext()) {

			Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>) it.next();

			String key = entry.getKey();
			String[] value = entry.getValue();
			switch (key) {
			case Constants.user_name:
				user_name = value[0].toString();
				break;
			case Constants.roll_no:
				user_id = value[0].toString();
				break;
			case Constants.email:
				email = value[0].toString();
				break;
			case Constants.ph_no:
		     	ph_no = value[0].toString();
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
			b = DataManager.edit(connection,user_id,user_name,ph_no,email);
			if (b) {
				Logininfo list=DataManager.getedit(connection,user_id);
				JsonObject json = new JsonObject();
				json.addProperty("success", "1");
				json.addProperty("message", "User successfully updated");
				json.addProperty("user_name", list.getUsername());
				json.addProperty("ph_no", list.getPhno());
				json.addProperty("email", list.getEmail());
				response.getWriter().write(json.toString());
			} 
			else if (!b) {
				JsonObject json = new JsonObject();
				json.addProperty("sucess", "0");
				json.addProperty("message", "Oops an error occured");
				response.getWriter().write(json.toString());

			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		CacheConnection.checkIn(connection);

	}

	}


