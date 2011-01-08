package com.github.simplenotelib;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Logger;

public class SimpleNoteAPI {
	private static final String LOG_TAG = "SimpleNoteAPI";
	public static final String BASE_URL = "https://simple-note.appspot.com/api";
	public static final String LOGIN_PATH = "/login";
	public static final String DATA_PATH = "/data";
	public static final String INDEX_PATH = "/index";
	
	public static Logger Log = Logger.getLogger(LOG_TAG);
	private String email;
	private String password;
	
	@SuppressWarnings("unused")
	private String authToken;
	public String getAuthToken() {
		return this.authToken;
	}
	public SimpleNoteAPI(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	private BufferedReader connect(String url, String body) {
		BufferedReader buffer = null;
		try {
			URL u = new URL(url);
			URLConnection ucon = u.openConnection();
			ucon.setDoOutput(true);
			OutputStream out = null;
			try {
				out = ucon.getOutputStream();
				out.write(body.getBytes());
			} finally {
				if (out != null) try { out.close(); } catch (IOException logOrIgnore) {}
			}
			buffer = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
		} catch(MalformedURLException me) {
			Log.info("Generated malformed url " + url);
			// TODO: Should provide some kind of error message for the user
			// this would also mean something is really broken and should get 
			// logged to an online server so that can fix later on.
		} catch (IOException e) {
			// TODO: Possible retry for x times before falure since could be network issue
			Log.info("Connection failure " + url + "\nException Message: " + e.getMessage());
		}
		
		return buffer;
	}
	
	private String requestURL(String url, String body) throws IOException {
		BufferedReader buff = connect(url, body);
		ByteArrayOutputStream baf = new ByteArrayOutputStream(50);
		int current = 0;
		while((current = buff.read()) != -1){
			baf.write((byte)current);
		}
		return new String(baf.toByteArray()); 
	}
	
	
	public void login() {
		String url = BASE_URL + LOGIN_PATH; 
		String body="email=" + this.email + "&password=" + this.password;
		try {
			this.authToken = requestURL(url, body);
		} catch(IOException e) {
			Log.info("IO login failure");
			// TODO: Retry a couple times then display message?
		}
	}
	
}
