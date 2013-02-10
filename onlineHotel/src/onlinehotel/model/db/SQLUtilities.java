package onlinehotel.model.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLUtilities {

	public static Connection connect() {
		Connection connection = null;
		try {
			// Load the JDBC driver
			String driverName = "com.mysql.jdbc.Driver"; // MySQL MM JDBC driver
			Class.forName(driverName);
			// Create a connection to the database
			String url = "jdbc:mysql://localhost:3308/hoteldb?useUnicode=true&characterEncoding=utf8";
			String username = "onlineHotel";
			String password = "onlineHotel";
			connection = DriverManager.getConnection(url, username, password);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static ResultSet executeSQL(String query, Connection connection) {
		if (connection != null) {
			try {
				Statement statement = connection.createStatement();
				statement.execute(query);
				return statement.getResultSet();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	public static int executeInsert(String query, Connection connection) {
		if (connection != null) {
			try {
				Statement statement = connection.createStatement();
				statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
				ResultSet generatedKeys = statement.getGeneratedKeys();
				if (generatedKeys.next()) {
		            return generatedKeys.getInt(1);
		        } else {
		            return -1;
		        }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	public static boolean executeUpdate(String query, Connection connection) {
		if (connection != null) {
			try {
				Statement statement = connection.createStatement();
				int num = statement.executeUpdate(query);
				if (num > 0) {
		            return true;
		        } else {
		            return false;
		        }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public static void disconnect(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
