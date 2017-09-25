package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sun.org.apache.xml.internal.security.utils.Base64;

import pojo.Event;
import pojo.Fest;
import pojo.Logininfo;
import pojo.Registerinfo;
import pojo.Registration;
import utils.Logger;
import utils.PasswordAuthentication;

public class DataManager {
	public static boolean login(Connection connection, String roll_no, String password, String deviceid)
			throws Exception {
		System.out.println(roll_no);
		ResultSet rs = null;
		PreparedStatement statement = null;
		String data = "-1", t = "-1", passwd;
		String query = "select * from user_dimn where roll_no=?";
		boolean login = false;
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, roll_no);
			rs = statement.executeQuery();
			if (rs.next()) {
				data = rs.getString("user_id");
				passwd = rs.getString("pswd");
				t = rs.getString("isadmin");
				if (passwd.equals(password)) {
					login(connection, data, deviceid);
					login = true;
				}
			}

		} catch (Exception e) {
			// Logger.log(e);
			e.printStackTrace();
			throw e;
		}
		return login;
	}

	private static ArrayList<String> loginadmin(Connection connection, int i) throws SQLException {
		ArrayList<String> arrayList = new ArrayList<String>();
		ResultSet rs = null;
		PreparedStatement statement = null;
		String s = "-1";
		String query = "select * from admin_dimn where user_id=?";
		try {
			statement = connection.prepareStatement(query);
			statement.setInt(1, i);
			rs = statement.executeQuery();
			while (rs.next()) {
				s = rs.getString("event_id");
				arrayList.add(s);
			}
		} catch (SQLException e) {
			// Logger.log(e);
			throw e;
		}
		return arrayList;
	}

	private static void login(Connection connection, String user_id, String device_id) throws SQLException {
		PreparedStatement statement = null;
		String s = "-1";
		PreparedStatement statement1 = null;
		String query1 = "delete from active_session where user_id=?;";
		String query = "insert into active_session(user_id,device_id) values(?,?) ";
		try {

			statement1 = connection.prepareStatement(query1);
			statement1.setString(1, user_id);
			statement1.executeUpdate();
			statement = connection.prepareStatement(query);
			statement.setString(1, user_id);
			statement.setString(2, device_id);
			statement.executeUpdate();
		} catch (SQLException e) {
			// Logger.log(e);
			throw e;
		}
	}

	public static boolean logout(Connection connection, String device_id) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement statement = null;

		String query = "delete from active_session where device_id=? ";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, device_id);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			// Logger.log(e);
			return false;

		}
	}

	public static Boolean create(Connection connection, Logininfo login) throws SQLException {
		try {
			PreparedStatement statement = null;

			String query = "insert into user_dimn(roll_no,user_name,department,ph_no,email,pswd,isadmin)values (?,?,?,?,?,?,?) ";

			statement = connection.prepareStatement(query);
			statement.setString(1, login.getRollno());
			statement.setString(2, login.getUsername());
			statement.setString(3, login.getDepartment());
			statement.setString(4, login.getPhno());
			statement.setString(5, login.getEmail());
			statement.setString(6, login.getPswd());
			statement.setInt(7, login.getIsadmin());
			int i = statement.executeUpdate();
			if (i > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			// Logger.log(e);
			e.printStackTrace();
			return false;
		}
	}

	public static Logininfo getRecord(Connection connection, String roll_no) {

		String strSelect = "select * from user_dimn where roll_no=?";
		try {
			PreparedStatement ps = connection.prepareStatement(strSelect);
			ps.setString(1, roll_no);
			ResultSet rs = ps.executeQuery();
			Logininfo data = null;

			if (rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					data = new Logininfo();
					data.setUserid(rs.getInt(1));
					data.setUsername(rs.getString(3));
					data.setPhno(rs.getString(5));
					data.setEmail(rs.getString(6));
					data.setIsadmin(rs.getInt(8));
					if (data.getIsadmin() == 1) {
						ArrayList<String> array = loginadmin(connection, data.getUserid());
						System.out.println(array.size() + "");
						String a = "";
						for (String s : array)
							a = a + s + ";";
						if (a.endsWith(";"))
							a = a.substring(0, a.length() - 1);
						data.setFest_id(a);
					}
				}
			}

			return data;
		} catch (Exception ex) {
			System.out.print(ex);
			return null;
		}
	}

	public static Boolean edit(Connection connection, String User_id, String User_name, String ph_no, String email) {

		String strSelect = "update user_dimn set user_name=?, ph_no=?, email=? where roll_no=?";
		try {
			PreparedStatement ps = connection.prepareStatement(strSelect);
			ps.setString(1, User_name);
			ps.setString(2, ph_no);
			ps.setString(3, email);
			ps.setString(4, User_id);
			int i = ps.executeUpdate();
			if (i > 0)
				return true;

			else
				return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}

	public static Logininfo getedit(Connection connection, String user_id) throws SQLException {

		String strSelect = "select * from user_dimn where roll_no=?";
		try {
			PreparedStatement ps = connection.prepareStatement(strSelect);
			ps.setString(1, user_id);
			ResultSet rs = ps.executeQuery();
			Logininfo data = null;

			if (rs.next()) {
				rs.beforeFirst();
				while (rs.next()) {
					data = new Logininfo();
					data.setUsername(rs.getString(3));
					data.setPhno(rs.getString(5));
					data.setEmail(rs.getString(6));

				}
			}

			return data;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Boolean registration(Connection connection, Registerinfo register) throws SQLException {
		try {
			PreparedStatement statement = null;

			String query = "insert into registration(event_id,user_id) values(?,?) ";

			statement = connection.prepareStatement(query);
			statement.setInt(1, register.getEvent_id());
			statement.setInt(2, register.getUser_id());

			int i = statement.executeUpdate();
			if (i > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			// Logger.log(e);
			e.printStackTrace();
			return false;
		}
	}

	public static Boolean unregister(Connection connection, String user_id, String event_id) throws SQLException {
		// TODO Auto-generated method stub
		PreparedStatement statement = null;

		String query = "delete from registration where user_id=? and event_id=?";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, user_id);
			statement.setString(2, event_id);
			int i = statement.executeUpdate();
			if (i > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			// Logger.log(e);
			e.printStackTrace();
			return false;

		}
	}

	public static String getResult(Connection connection, String event_id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement statement = null;
		String s = "-1";
		String query = "SELECT * FROM result where event_id=?";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, event_id);
			rs = statement.executeQuery();
			while (rs.next()) {
				s = rs.getString("results");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static ArrayList<Event> getClubEvents(Connection connection) throws SQLException {
		ResultSet rs = null;
		PreparedStatement statement = null;
		ArrayList<Event> events = new ArrayList<Event>();
		String query = "SELECT * FROM event_dimn where fest_id=-1";
		try {
			statement = connection.prepareStatement(query);
			rs = statement.executeQuery();
			while (rs.next()) {
				Event eve = new Event();
				eve.setFest_id(rs.getString("fest_id"));
				eve.setEvent_name(rs.getString("event_name"));
				eve.setEvent_desc(rs.getString("event_desc"));
				eve.setVenue(rs.getString("venue"));	
				String ts = rs.getString("start_time").toString();
				ts = ts.substring(0, ts.indexOf('.'));
				eve.setStart_time(ts);
				ts = rs.getString("end_time").toString();
				ts = ts.substring(0, ts.indexOf('.'));
				eve.setEnd_time(ts);
				eve.setEvent_id(rs.getInt("event_id"));
				eve.setClub_name(rs.getString("club_name"));
				events.add(eve);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return events;
	}
	public static ArrayList<Event> getEventsByIds(Connection connection, String event_ids) throws SQLException {
		ResultSet rs = null;
		PreparedStatement statement = null;
		ArrayList<Event> events = new ArrayList<Event>();
		String query = "SELECT * FROM event_dimn where event_id in ("+event_ids+")";
		if("-1".equals(event_ids))
			query = "SELECT * FROM event_dimn where fest_id=-1";
		try {
			statement = connection.prepareStatement(query);
			rs = statement.executeQuery();
			rs.beforeFirst();
			while (rs.next()) {
				Event eve = new Event();
				eve.setFest_id(rs.getString("fest_id"));
				eve.setEvent_name(rs.getString("event_name"));
				eve.setEvent_desc(rs.getString("event_desc"));
				eve.setVenue(rs.getString("venue"));
				eve.setEvent_id(rs.getInt("event_id"));
				events.add(eve);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return events;
	}
	public static ArrayList<Event> getEvents(Connection connection, String fest_id) throws SQLException {
		ResultSet rs = null;
		PreparedStatement statement = null;
		ArrayList<Event> events = new ArrayList<Event>();
		String query = "SELECT * FROM event_dimn where fest_id=?";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, fest_id);
			rs = statement.executeQuery();
			while (rs.next()) {
				Event eve = new Event();
				eve.setFest_id(rs.getString("fest_id"));
				eve.setEvent_name(rs.getString("event_name"));
				eve.setEvent_desc(rs.getString("event_desc"));
				eve.setVenue(rs.getString("venue"));
				String ts = rs.getString("start_time").toString();
				ts = ts.substring(0, ts.indexOf('.'));
				eve.setStart_time(ts);
				ts = rs.getString("end_time").toString();
				ts = ts.substring(0, ts.indexOf('.'));
				eve.setEnd_time(ts);
				eve.setEvent_id(rs.getInt("event_id"));

				events.add(eve);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return events;
	}

	public static boolean uploadResult(Connection connection, String event_id, String results) throws SQLException {
		PreparedStatement statement = null;
		String query = "insert into result(event_id,results) values(?,?)";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, event_id);
			statement.setString(2, results);
			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			throw e;

		}

	}

	public static ArrayList<Event> getRegisteredEvent(Connection connection, String user_id) throws SQLException {
		ArrayList<Event> events = new ArrayList<Event>();
		ResultSet rs1 = null;
		PreparedStatement statement = null;
		String event_id = "-1";

		String query = "SELECT * FROM registration where user_id=?";
		try {
			statement = connection.prepareStatement(query);
			statement.setString(1, user_id);
			rs1 = statement.executeQuery();
			while (rs1.next()) {
				event_id = rs1.getString("event_id");

				ResultSet rs = null;

				String secondQuery = "SELECT * FROM event_dimn where event_id=?";
				try {
					statement = connection.prepareStatement(secondQuery);
					statement.setString(1, event_id);
					rs = statement.executeQuery();
					while (rs.next()) {
						Event eve = new Event();
						eve.setFest_id(rs.getString("fest_id"));
						eve.setEvent_name(rs.getString("event_name"));
						eve.setEvent_desc(rs.getString("event_desc"));
						eve.setVenue(rs.getString("venue"));
						String ts = rs.getString("start_time").toString();
						ts = ts.substring(0, ts.indexOf('.'));
						eve.setStart_time(ts);
						ts = rs.getString("end_time").toString();
						ts = ts.substring(0, ts.indexOf('.'));
						eve.setEnd_time(ts);
						eve.setEvent_id(rs.getInt("event_id"));

						events.add(eve);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return events;
	}

	public static ArrayList<Registration> getregistration(Connection connection, String event_id) {
		String strSelect = "SELECT user_id FROM registration where event_id=?";
		ArrayList<Registration> list = new ArrayList<Registration>();
		;
		try {
			PreparedStatement ps = connection.prepareStatement(strSelect);
			ps.setString(1, event_id);
			ResultSet rs = ps.executeQuery();

			Registration data = null;
			int userid;
			while (rs.next()) {
				userid = rs.getInt("user_id");
				String strSelectall = "SELECT * FROM user_dimn where user_id=?";

				PreparedStatement pss = connection.prepareStatement(strSelectall);
				pss.setInt(1, userid);
				ResultSet rss = pss.executeQuery();

				while (rss.next()) {
					data = new Registration();
					data.setUserName(rss.getString(3));
					data.setPhNo(rss.getString(5));
					data.setEmail(rss.getString(6));
					data.setDepartment(rss.getString(4));
					data.setRollNo(rss.getString(2));
					list.add(data);
				}

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return list;
	}

	public ArrayList<Fest> getfest(Connection connection) {

		String strSelect = "SELECT * FROM fest_dimn";

		ArrayList<Fest> list = new ArrayList<>();
		try {
			PreparedStatement ps = connection.prepareStatement(strSelect);

			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				list = new ArrayList<Fest>();
				Fest data;
				rs.beforeFirst();
				while (rs.next()) {
					data = new Fest();
					data.setFestId(rs.getInt(1) + "");
					data.setFestName(rs.getString(2));
					list.add(data);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			;

		}
		return list;

	}
	public static Boolean addevents(Connection connection, Event add) throws SQLException {
		try {
			PreparedStatement statement = null;

			String query = "insert into event_dimn(fest_id,event_name,event_desc,venue,start_time,end_time,result_available,club_name)values (?,?,?,?,?,?,?,?) ";

			statement = connection.prepareStatement(query);
			statement.setString(1, add.getFest_id());
			statement.setString(2,add.getEvent_name()) ;
			statement.setString(3,add.getEvent_desc() );
			statement.setString(4, add.getVenue());
			statement.setString(5,add.getStartTime());
			statement.setString(6,add.getEndTime());
			statement.setInt(7,add.getResult_available());
			statement.setString(8, add.getClub_name());
			
			
			int i = statement.executeUpdate();
			if (i > 0)
				return true;
			else
				return false;
		} catch (SQLException e) {
			// Logger.log(e);
			e.printStackTrace();
			return false;
		}
	}

}
