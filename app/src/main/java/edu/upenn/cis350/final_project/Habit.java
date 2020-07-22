package edu.upenn.cis350.final_project;

import java.util.*;

enum Category {
    MENTAL_HEALTH, FITNESS, DIET, SLEEP, SOCIAL, OTHER
}

public class Habit {
    private String id;
    private String user;
    private Category category;
    private String goal;
    private int timesPerWeekGoal;
    private int timesThisWeek;
    private int priority;
    private String lastUpdated;

    public Habit() {
        this.category = null;
        this.goal = "";
        this.timesPerWeekGoal = -1;
        this.timesThisWeek = 0;
        this.lastUpdated = null;
    }

    public Habit(String id, String username, String category, String goal, int timesPerWeekGoal, int timesThisWeek,
                 int priority, String lastUpdated) {
        this.id = id;
        this.user = username;
        this.category = stringToCategory(category);
        this.goal = goal;
        this.timesPerWeekGoal = timesPerWeekGoal;
        this.timesThisWeek = timesThisWeek;
        this.priority = priority;
        this.lastUpdated = lastUpdated;
    }

    public String getId() {
        return this.id;
    }

    public String getUser() {
        return this.user;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getGoal() {
        return this.goal;
    }

    public int getTimesPerWeekGoal() {
        return this.timesPerWeekGoal;
    }

    public int getTimesThisWeek() {
        return this.timesThisWeek;
    }

    public String getLastUpdated() {
        return this.lastUpdated;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setTimesPerWeekGoal(int timesPerWeekGoal) {
        this.timesPerWeekGoal = timesPerWeekGoal;
    }

    public void setTimesThisWeek(int timesThisWeek) {
        this.timesThisWeek = timesThisWeek;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Category stringToCategory(String category) {
        if (category.equalsIgnoreCase("mental health")
                || category.equalsIgnoreCase("mental")){
            return Category.MENTAL_HEALTH;
        } else if (category.equalsIgnoreCase("fitness")){
            return Category.FITNESS;
        } else if (category.equalsIgnoreCase("diet")){
            return Category.DIET;
        } else if (category.equalsIgnoreCase("sleep")){
            return Category.SLEEP;
        } else if (category.equalsIgnoreCase("social")){
            return Category.SOCIAL;
        } else if (category.equalsIgnoreCase("other")){
            return Category.OTHER;
        }
        return Category.OTHER;
    }

    public String categoryToString(Category category) {
        if (category == Category.MENTAL_HEALTH) {
            return "mental";
        } else if (category == Category.FITNESS) {
            return "fitness";
        } else if (category == Category.DIET) {
            return "diet";
        } else if (category == Category.SLEEP) {
            return "sleep";
        } else if (category == Category.SOCIAL) {
            return "social";
        } else if (category == Category.OTHER) {
            return "other";
        }
        return "other";
    }
}
