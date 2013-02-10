package testcases;

import onlinehotel.users.UserManager;
import junit.framework.TestCase;

public class UserManagerTest extends TestCase {

	private UserManager manager = null;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		manager = new UserManager();
	}

	public void testEmployees() {
		assertTrue(manager.isValidEmployee("empl1", "empl1pass"));
		assertTrue(manager.isValidEmployee("empl2", "empl2pass"));
		assertFalse(manager.isValidEmployee("empl1", "empl2pass"));
	}
	
	public void testAdmins() {
		assertTrue(manager.isValidAdmin("admin1", "admin1pass"));
		assertFalse(manager.isValidAdmin("admin1", "admin2pass"));
	}
}
