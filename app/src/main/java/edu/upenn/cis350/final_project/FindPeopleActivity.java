package edu.upenn.cis350.final_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class FindPeopleActivity extends AppCompatActivity {
    Button newbtn;
    String currentUser;
    String peopleString;
    ArrayList<String> peopleObjectArray = new ArrayList<>();
    ArrayList<String> followingUsers = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_people);
        currentUser = getIntent().getStringExtra("username").toString();
        findPeople();
        parsePeople();
    }
    public void parsePeople(){
        System.out.println("Now executing parsePeople");
        System.out.println("This is peopleString:" + peopleString);
        peopleObjectArray = new ArrayList<>();
        followingUsers = new ArrayList<>();
        try {
            JSONParser parser = new JSONParser();
            JSONArray data = (JSONArray)parser.parse(peopleString);
            for(Object entry : data){
                JSONObject entryConverted = (JSONObject)entry;
                peopleObjectArray.add((String)entryConverted.get("username"));
                if(((String)entryConverted.get("username")).equals(currentUser)){
                    JSONArray following = (JSONArray) entryConverted.get("following");
                    followingUsers = new ArrayList<>();
                    for(Object o : following){
                        followingUsers.add((String) o);
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void findPeople()  {
        String link = "http://10.0.2.2:3000/findAllUsers";
        URL url = null;
        try{
            url = new URL(link);
        }catch (Exception e){
            e.printStackTrace();
        }
        @SuppressLint("StaticFieldLeak")
        final AsyncTask<URL, String, String> findAllTask= new AsyncTask<URL, String, String>(){
            String line = "";
            protected String doInBackground(URL...urls){
                URL url;
                HttpURLConnection conn;
                Scanner in = null;
                //Try to establish connection
                try {
                    url = urls[0];
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.connect();
                    in = new Scanner(url.openStream());
                    while(in.hasNext()){
                        line = in.nextLine();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return line;
            }
            public void onPostExecute(String input) {
            }
        };
        findAllTask.execute(url);
        try{
            peopleString = findAllTask.get();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void refreshButton(View v) throws MalformedURLException {
        findPeople();
        parsePeople();
        LinearLayout layout = (LinearLayout)findViewById(R.id.rootlayout);
        layout.removeViews(1, layout.getChildCount() - 1);
        for(final String user : peopleObjectArray){
            if(user.equals(currentUser) || followingUsers.contains(user)){
                continue;
            }
            newbtn = new Button(this );
            newbtn.setText(user);
            newbtn.setOnClickListener(new View.OnClickListener() {
                String link = "http://10.0.2.2:3000/updateFollow?username="+currentUser+"&followed="+user;
                URL url = new URL(link);
                @Override
                public void onClick(View v) {
                    v.setEnabled(false);
                    System.out.println(currentUser + " has clicked " + user);
                    @SuppressLint("StaticFieldLeak")
                    final AsyncTask<URL, String, String> followTask = new AsyncTask<URL, String, String>() {
                        @Override
                        protected String doInBackground(URL... urls) {
                            URL url;
                            HttpURLConnection conn;
                            Scanner in = null;
                            try {
                                url = urls[0];
                                conn = (HttpURLConnection) url.openConnection();
                                conn.setRequestMethod("GET");
                                conn.connect();
                                in = new Scanner(url.openStream());
                                System.out.println("Connection Made with " + url);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return "";
                        }
                    };
                    followTask.execute(url);
                }
            });
            layout.addView(newbtn);
        }
        ;    }
}