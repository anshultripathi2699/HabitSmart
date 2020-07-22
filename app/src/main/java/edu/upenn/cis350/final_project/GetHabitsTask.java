package edu.upenn.cis350.final_project;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.util.*;
import java.net.URL;

public class GetHabitsTask extends AsyncTask<URL, String, Set<Habit>> {

    protected Set<Habit> doInBackground(URL... urls) {

        try {
            System.out.println("Running GetHabitsTask...");
            URL url = urls[0];
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            Set<Habit> habits = new HashSet<>();

            Scanner in = new Scanner(url.openStream());
            String line = in.nextLine();
            JSONArray habitsArray = new JSONArray(line);
            for (int i = 0; i < habitsArray.length(); i++) {

                JSONObject jsonHabit = habitsArray.getJSONObject(i);

                String id = jsonHabit.getString("_id");
                String user = jsonHabit.getString("user");
                String category = jsonHabit.getString("category");
                String goal = jsonHabit.getString("goal");
                int timesPerWeekGoal = jsonHabit.getInt("timesPerWeekGoal");
                int timesThisWeek = jsonHabit.getInt("timesThisWeek");
                int priority = jsonHabit.getInt("priority");

                String lastUpdated = jsonHabit.getString("lastUpdated");

                Habit h = new Habit(id, user, category, goal, timesPerWeekGoal,
                        timesThisWeek, priority, lastUpdated);
                habits.add(h);
            }

            return habits;
        } catch (Exception e) {
            System.out.println("Catching exception in GetHabitsTask");
            e.printStackTrace();;
            return null;
        }

    }

}
