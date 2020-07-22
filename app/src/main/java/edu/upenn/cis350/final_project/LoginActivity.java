package edu.upenn.cis350.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.*;
import java.net.*;

public class LoginActivity extends AppCompatActivity {
    int buttonClickActivityID = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLogInButtonClick(View v) throws Exception {
        System.out.println("Log in");
        //Extract username
        EditText username_entry = (EditText) findViewById(R.id.username);
        String username = username_entry.getText().toString();
        //Extracts password
        EditText password_entry = (EditText) findViewById(R.id.password);
        String password = password_entry.getText().toString();
        //Check whether username-password is valid
        boolean authentication = authenticate(username, password);
        //Proceed if authentic
        if(authentication){
            Intent i = new Intent(this, MainActivity.class);
            i.putExtra("username", username);
            startActivityForResult(i, buttonClickActivityID);
        }else{
            username_entry.setText("");
            password_entry.setText("");
            Toast.makeText(getApplicationContext(), "Invalid username/password", Toast.LENGTH_LONG).show();
        }
    }

    public void onCreateAccountButtonClick(View v) throws Exception {
        EditText username_entry = (EditText) findViewById(R.id.username);
        String username = username_entry.getText().toString();
        EditText password_entry = (EditText) findViewById(R.id.password);
        String password = password_entry.getText().toString();
        boolean creation = create(username, password);
        if(creation){
            Toast.makeText(getApplicationContext(), "Created", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "Could not create", Toast.LENGTH_LONG).show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public boolean create(String username, String password) throws Exception {
        String linkCheck = "http://10.0.2.2:3000/find?username="+username;
        URL urlCheck = new URL(linkCheck);
        final AsyncTask<URL, Boolean, Boolean> checkTask = new AsyncTask<URL, Boolean, Boolean>(){
            protected Boolean doInBackground(URL...urls){
                URL url;
                HttpURLConnection conn;
                Scanner in = null;
                boolean result = false;
                try {
                    url = urls[0];
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    in = new Scanner(url.openStream());
                    while(in.hasNext()){
                        String line = in.nextLine();
                        System.out.println("This is checked username result: " + line);
                        if(line.equals("null")){
                            System.out.println("This has been approved");
                            result = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return result;
            }
            public void onPostExecute(String input){
            }
        };
        checkTask.execute(urlCheck);
        boolean checkResult = checkTask.get();
        if(!checkResult){
            return false;
        }
        String link = "http://10.0.2.2:3000/createUser?username="+username+"&password="+password;
        URL url = new URL(link);
        final AsyncTask<URL, Boolean, Boolean> createTask= new AsyncTask<URL, Boolean, Boolean>(){
            protected Boolean doInBackground(URL...urls){
                URL url;
                HttpURLConnection conn;
                Scanner in = null;
                boolean result = false;
                //Try to establish connection
                try {
                    url = urls[0];
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    in = new Scanner(url.openStream());
                    while(in.hasNext()){
                        String line = in.nextLine();
                        if(line.equals("Oops")){
                            result = false;
                        }else if(line.equals("Done")){
                            result = true;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
            public void onPostExecute(String input) {
            }
        };
        createTask.execute(url);
        boolean result = createTask.get();
        return result;
    }

    @SuppressLint("StaticFieldLeak")
    public boolean authenticate(final String username, final String password) throws Exception {
        //Construct new localhost link to locate the user
        String link = "http://10.0.2.2:3000/find?username="+username;
        URL url = new URL(link);
        final AsyncTask<URL, Boolean, Boolean> authenticateTask = new AsyncTask<URL, Boolean, Boolean>() {
            protected Boolean doInBackground(URL... urls) {
                URL url;
                HttpURLConnection conn;
                Scanner in = null;
                boolean result = false;
                //Try to establish connection
                try {
                    url = urls[0];
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    in = new Scanner(url.openStream());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //By this point the connection should have been established
                //Now we extract the password and see if it matches
                String line = "";
                while (in.hasNext()) {
                    line = in.nextLine();
                    if (line.equals("null")) {
                        result = false;
                    }else{
                        JSONParser parser = new JSONParser();
                        try {
                            JSONObject data = (JSONObject)parser.parse(line);
                            String retrieved_password = (String)data.get("password");
                            result = retrieved_password.equals(password);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                return result;
            }
            public void onPostExecute(String input) {
            }
        };
        authenticateTask.execute(url);
        boolean result = authenticateTask.get();
        return result;
    };


}
