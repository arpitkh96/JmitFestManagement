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
import pojo.Fest;

/**
 * Servlet implementation class Getfest
 */
@WebServlet("/get_fests")
public class Getfest extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Getfest() {
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
		CacheConnection.setVerbose(true);

		// Get a cached connection
		java.sql.Connection connection = CacheConnection.checkOut();
		try {
			connection.setReadOnly(true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ArrayList<Fest>list = null;
		try {
			
			list=new DataManager().getfest(connection);
			if (list!=null) {
				JsonObject json = new JsonObject();
				json.addProperty("success", "1");
		     	json.addProperty("fests", gson.toJson(list));
				response.getWriter().write(json.toString());
			} else  {
				JsonObject json = new JsonObject();
				json.addProperty("success", "0");
				json.addProperty("message", "No active fest found");
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

	

