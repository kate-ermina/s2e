package onlinehotel.users;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class UserManager {

	private Properties employees;
	private Properties admins;
	
	public UserManager() {
		employees = new Properties();
		admins = new Properties();
		try{
			InputStream in = UserManager.class.getResourceAsStream("employees.properties");
			employees.load(in);
			in.close();
			
			in = UserManager.class.getResourceAsStream("admins.properties");
			admins.load(in);
			in.close();
		} catch (IOException ex) {
			System.err.println("Cannot load users!");
		}
	}
	
	public boolean isValidAdmin(String username, String password) {
		String realPassword = admins.getProperty(username);
		if (realPassword != null && realPassword.equals(password)) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isValidEmployee(String username, String password) {
		String realPassword = employees.getProperty(username);
		if (realPassword != null && realPassword.equals(password)) {
			return true;
		} else {
			return false;
		}
	}
}
