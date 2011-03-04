package com.github.simplenotelib;

import java.io.BufferedReader;
import sun.misc.BASE64Encoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Logger;

import com.google.gson.Gson;

public class SimpleNoteAPI {
    public static final String BASE_URL = "https://simple-note.appspot.com/api2";

    public static final String LOGIN_URL = "https://simple-note.appspot.com/api/login";
    public static final String DATA_PATH = "/data";
    public static final String INDEX_PATH = "/index";

    public static Logger Log = Logger.getLogger(SimpleNoteAPI.class.getName());
    private String email;
    private String password;

    private String authToken;
    private Gson gson;
    public String getAuthToken() {
        return this.authToken;
    }
    public SimpleNoteAPI(String email, String password) {
        this.email = email;
        this.password = password;
        this.gson = new Gson();

     }

    private void writePostData(URLConnection connection, String data,
            boolean encode) throws IOException {
        connection.setDoOutput(true);
        OutputStream out = null;
        try {
            out = connection.getOutputStream();
            if(encode) {
                data = new BASE64Encoder().encode(data.getBytes());
            }
            out.write(data.getBytes());
            out.flush();
        } finally {
            if (out != null) try { out.close(); } catch (IOException logOrIgnore) {}
        }
    }
    private BufferedReader connect(String url, String body, boolean encode) throws IOException{
        BufferedReader buffer = null;
        try {
            URL u = new URL(url);
            URLConnection ucon = u.openConnection();
            if (body != null) {
                writePostData(ucon, body, encode);
            }
            buffer = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
        } catch(MalformedURLException me) {
            Log.info("Generated malformed url " + url);
            // TODO: Should provide some kind of error message for the user
            // this would also mean something is really broken and should get 
            // logged to an online server so that can fix later on.
        }

        return buffer;
    }

    private String requestURL(String url, String body, boolean encode) throws IOException {
        System.out.println("Body is: " + body);
        BufferedReader buff = connect(url, body, encode);
        ByteArrayOutputStream baf = new ByteArrayOutputStream(50);
        int current = 0;
        while((current = buff.read()) != -1){
            baf.write((byte)current);
        }
        String response = new String(baf.toByteArray()); 
        System.out.println("Repsonse is: " + response);
        return response;
    }


    public void login() throws IOException {
        String url = LOGIN_URL; 
        String body="email=" + this.email + "&password=" + this.password;

        this.authToken = requestURL(url, body, true);
    }
    public String getEmail() {
        return email;
    }
    private Note noteFromJSON(String json) {
        System.out.println("Made it before fromJSON");
        return this.gson.fromJson(json, Note.class);
    }

    public Note add(Note note) throws IOException {
        String url = BASE_URL + DATA_PATH + "?auth=" + this.authToken + "&email=" + this.email;
        String body = gson.toJson(note);
        String newNoteJSON = requestURL(url, body, false);
        return noteFromJSON(newNoteJSON);
    }

    public Note get(String key) throws IOException {
        String url = BASE_URL + DATA_PATH + "/" + key + "?auth=" + this.authToken + "&email=" + this.email;
        String noteJSON = requestURL(url, null, false);
        return noteFromJSON(noteJSON);
    }

}
