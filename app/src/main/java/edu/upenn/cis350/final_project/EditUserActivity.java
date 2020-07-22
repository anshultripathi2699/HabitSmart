package edu.upenn.cis350.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.*;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import android.view.*;
import android.content.Intent;
import java.util.Random;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class EditUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
    }

    public void onSaveButtonClick(View v) throws Exception {
        String username_old = getIntent().getStringExtra("username").toString();
        //////////
        String username_new = ((EditText)findViewById(R.id.newusername)).getText().toString();
        String firstname_new = ((EditText)findViewById(R.id.firstnamenew)).getText().toString();
        String lastname_new = ((EditText)findViewById(R.id.lastnamenew)).getText().toString();
        String password_new = ((EditText)findViewById(R.id.passwordnew)).getText().toString();
        String gender_new = ((EditText)findViewById(R.id.gendernew)).getText().toString();
        String email_new = ((EditText)findViewById(R.id.emailnew)).getText().toString();

        String link = "http://10.0.2.2:3000/update";
        link = link + "?uo="+username_old;
        link = link + "&un=" + username_new;
        link = link + "&fnn=" + firstname_new;
        link = link + "&lnn=" + lastname_new;
        link = link + "&pn=" + password_new;
        link = link + "&gn=" + gender_new;
        link = link + "&emn=" + email_new;

        URL urlUpdate = new URL(link);

        @SuppressLint("StaticFieldLeak")
        final AsyncTask<URL, Boolean, Boolean> updateTask = new AsyncTask<URL, Boolean, Boolean>(){
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
                    System.out.println("Connection Made with " + url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
            public void onPostExecute(String input){
            }
        };
        updateTask.execute(urlUpdate);
        Toast.makeText(getApplicationContext(),"Saved! Must Restart Now!", Toast.LENGTH_LONG).show();
    }
}
