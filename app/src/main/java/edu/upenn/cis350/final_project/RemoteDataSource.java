package edu.upenn.cis350.final_project;

import java.util.*;
import java.net.*;
import org.json.*;

public class RemoteDataSource {
    private String protocol;
    private String host;
    private int port;

    public RemoteDataSource() {
        this.protocol = "http://";
        this.host = "10.0.2.2";
        this.port = 3000;
    }

    public User getUser(String username) {
        try {
            String urlStr = buildFindUserURL(username);
            System.out.println(urlStr);
            URL url = new URL(urlStr);
            GetUserTask task = new GetUserTask();
            task.execute(url);
            User user = task.get();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildFindUserURL(String username) {
        return (this.protocol + this.host + ":" + this.port + "/getUser/get?user=" + username);
    }

    public Set<Habit> getHabits(String username) {
        try  {
            URL url = new URL(buildFindHabitsURL(username));
            GetHabitsTask task = new GetHabitsTask();
            task.execute(url);
            Set<Habit> habits = task.get();
            return habits;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String buildFindHabitsURL(String username) {
        return (this.protocol + this.host + ":" + this.port + "/getHabits/get?user=" + username);
    }

    public void addHabit(Habit habit) {
        try {
            String urlStr = buildAddHabitURL(habit);
            System.out.println(urlStr);
            URL url = new URL(urlStr);
            AddHabitTask task = new AddHabitTask();
            task.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String buildAddHabitURL(Habit habit) {
        String[] words = habit.getGoal().split(" ");
        StringBuilder goal = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            goal.append("-" + words[i].replaceAll("[^a-zA-Z0-9]", ""));
        }

        return (this.protocol + this.host + ":" + this.port + "/createHabit/get?"
        + "user=" + habit.getUser() + "&category=" + habit.categoryToString(habit.getCategory())
        + "&goal=" + goal.toString() + "&timesPerWeekGoal=" + habit.getTimesPerWeekGoal()
        + "&priority=" + habit.getPriority());
    }

    public void updateHabit(Habit habit) {
        try {
            String urlStr = buildUpdateHabitURL(habit);
            System.out.println(urlStr);
            URL url = new URL(urlStr);
            AddHabitTask task = new AddHabitTask();
            task.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String buildUpdateHabitURL(Habit habit) {
        String[] words = habit.getGoal().split(" ");
        StringBuilder goal = new StringBuilder(words[0]);
        for (int i = 1; i < words.length; i++) {
            goal.append("-" + words[i].replaceAll("[^a-zA-Z0-9]", ""));
        }

        return (this.protocol + this.host + ":" + this.port + "/updateHabit/get?"
        + "id=" + habit.getId() + "&category=" + habit.categoryToString(habit.getCategory())
                + "&goal=" + goal.toString() + "&timesPerWeekGoal=" + habit.getTimesPerWeekGoal()
                + "&timesThisWeek=" + habit.getTimesThisWeek() + "&priority=" + habit.getPriority());
    }

}
