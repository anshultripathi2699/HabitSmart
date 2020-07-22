package edu.upenn.cis350.final_project;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateHabitTask extends AsyncTask<URL, String, Boolean> {

    protected Boolean doInBackground(URL ... urls) {

        try {
            System.out.println("Running UpdateHabitTask...");
            URL url = urls[0];
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            conn.getResponseCode();
            return true;
        } catch (Exception e) {
            System.out.println("catching exception in AddHabitTask");
            e.printStackTrace();
            return false;
        }

    }

}
