package database;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import utils.Logger;


public class JDBCMySQLConnection {
	private static JDBCMySQLConnection instance = new JDBCMySQLConnection();
	static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
	static final String URL = "jdbc:mysql://localhost:51708/a1344249_dev";
	static final String URL_1 = "jdbc:mysql://localhost:3306/backup_stats";
	static final String USER = "arpitkh96";//"root";
	static final String PASSWORD = "arpitkh96main";

	private JDBCMySQLConnection() {
        try {
            //Step 2: Load MySQL Java driver
            Class.forName(DRIVER_CLASS);
        } catch (ClassNotFoundException e) {
	        Logger.log(e);
	        }
    }

	private Connection createConnection() {
		Connection connection = null;
		try {
			Logger.log("Connecting"+(connection==null));
			connection = DriverManager.getConnection(URL, USER, PASSWORD);
			Logger.log("Connected"+(connection==null));
		} catch (SQLException e) {
			Logger.log(e);
			}
		return connection;
	}
	private Connection createConnectionForData() {
		Connection connection = null;
		try {
			Logger.log("Connecting"+(connection==null));
			connection = DriverManager.getConnection(URL_1, USER, PASSWORD);
			Logger.log("Connected"+(connection==null));
		} catch (SQLException e) {
			Logger.log(e);
			}
		return connection;
	}
	public static Connection getConnectionForData() {
		return instance.createConnectionForData();
	}
	public static Connection getConnection() {
		return instance.createConnection();
	}
}
