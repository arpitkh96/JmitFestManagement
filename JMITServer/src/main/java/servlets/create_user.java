package servlets;
import java.io.IOException;
import java.io.PrintWriter;
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
import pojo.Logininfo;

/**
 * Servlet implementation class create_user
 */
@WebServlet("/create_user")
public class create_user extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public create_user() {
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
		String password = null,rollno = null,username=null,department=null,phno=null,email=null;
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
			case Constants.user_name:
				username = value[0].toString();
				break;
			case Constants.department:
				department = value[0].toString();
				break;
			case Constants.email:
				email = value[0].toString();
				break;
			case Constants.ph_no:
		     	phno = value[0].toString();
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
			Logininfo data=new  Logininfo();
			data.setRollno(rollno);
			data.setUsername(username);
			data.setDepartment(department);
			data.setEmail(email);
			data.setPswd(password);
			data.setPhno(phno);
			data.setIsadmin((0));
			
			b = DataManager.create(connection, data);
			if (b) {
				JsonObject json = new JsonObject();
				json.addProperty("success", "1");
				json.addProperty("message", "user successfully registered");
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

