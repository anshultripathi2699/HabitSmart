package edu.upenn.cis350.final_project;

import android.os.AsyncTask;
import java.util.*;
import java.net.*;
import org.json.*;

public class GetUserTask extends AsyncTask<URL, String, User> {

    protected User doInBackground(URL ... urls) {

        try {
            System.out.println("Running GetUserTask...");
            URL url = urls[0];
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            Scanner in = new Scanner(url.openStream());
            String line = in.nextLine();
            JSONObject jsonUser = new JSONObject(line);
            String username = jsonUser.getString("username");
            String gender = jsonUser.getString("gender");

            JSONArray friendsArray = jsonUser.getJSONArray("following");
            List<String> friendsSet = new ArrayList<>();
            for (int i = 0; i < friendsArray.length(); i++) {
                friendsSet.add(friendsArray.getString(i));
            }

            User user = new User(username, gender, friendsSet);
            return user;

        } catch (Exception e) {
            System.out.println("catching exception in GetUserTask");
            e.printStackTrace();
            return null;
        }
    }
}
