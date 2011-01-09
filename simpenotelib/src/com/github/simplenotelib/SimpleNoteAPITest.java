package com.github.simplenotelib;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;


import junit.framework.Assert;
import junit.framework.TestCase;

public class SimpleNoteAPITest extends TestCase {
	Logger Log = Logger.getLogger("simplenotelibtest");
	String email;
	String password;
	protected void setUp() throws Exception {
		super.setUp();
		String home = System.getenv().get("HOME");

		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(home + "/simplenotestest.properties"));

			email = properties.getProperty("email");
			password = properties.getProperty("password");
		} catch (IOException e) {
			Log.info("Need a $HOME/simplenotestest.properties with email and password to test");
		}

	}

	public void testSimpleNoteAPI() {
		SimpleNoteAPI sn = new SimpleNoteAPI(email, password);
		Assert.assertNotNull(sn.getEmail());
	}

	public void testLogin() {
		SimpleNoteAPI sn = new SimpleNoteAPI(email, password);
		sn.login();
		Assert.assertNotNull(sn.getAuthToken());
	}

	public void testCreateNote() {
		SimpleNoteAPI sn = new SimpleNoteAPI(email, password);
		sn.login();

	}

}
