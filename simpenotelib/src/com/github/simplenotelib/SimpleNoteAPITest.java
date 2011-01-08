package com.github.simplenotelib;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


import junit.framework.Assert;
import junit.framework.TestCase;

public class SimpleNoteAPITest extends TestCase {

	String email;
	String password;
	protected void setUp() throws Exception {
		super.setUp();
		String home = System.getenv().get("HOME");
		/*
		Log.i("SimpleNoteeTest", "Home is " + home);
		Properties properties = new Properties();
		try {
			properties.load(new FileInputStream(home + "/simplenotestest.properties"));
			email = properties.getProperty("email");
			password = properties.getProperty("password");
		} catch (IOException e) {
		}

		*/
		
	}

	public void testSimpleNoteAPI() {
		SimpleNoteAPI sn = new SimpleNoteAPI(email, password);
		
	}

	public void testLogin() {
		SimpleNoteAPI sn = new SimpleNoteAPI(email, password);
		sn.login();
		Assert.assertNotNull(sn.getAuthToken());

		
	}

}
