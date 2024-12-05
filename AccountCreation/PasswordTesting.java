package AccountCreation;
import static org.junit.Assert.*;
import java.util.Set;
import java.util.HashSet;
import org.junit.Test;

import AccountCreation.RegistrationPage;


public class PasswordTesting{
	
	
	
	@Test
	public void passwordMatchTest() {
		RegistrationPage password = new RegistrationPage();
		assertEquals("Passwords match.", password.passwordChecker("Shockline","Shockline"));
		assertEquals("Passwords match.", password.passwordChecker("Tooth12","Tooth12"));
		assertEquals("Passwords match.", password.passwordChecker("Logic4","Logic4"));
		assertEquals("Passwords match.", password.passwordChecker("icetrip","icetrip"));
	}
	
	
	@Test
	public void passwordNotMatchTest() {
		RegistrationPage password = new RegistrationPage();
		assertEquals("Passwords do not match.", password.passwordChecker("coolness","collness"));
		assertEquals("Passwords do not match.", password.passwordChecker("pleas","peas"));
		assertEquals("Passwords do not match.", password.passwordChecker("Surefoot","surefoot"));
		assertEquals("Passwords do not match.", password.passwordChecker("Tail12","Tail32"));
	}
}

