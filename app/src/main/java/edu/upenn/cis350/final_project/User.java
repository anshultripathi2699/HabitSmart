package edu.upenn.cis350.final_project;
import java.util.*;

enum Gender {
    MALE, FEMALE;
}

public class User {
    private String username;
    private List<String> friends;
    private Set<Habit> habits;
    private Gender gender;

    public User(String username, String gender, List<String> friends) {
        this.username = username;
        if (gender.equals("M")) {
            this.gender = Gender.MALE;
        } else {
            this.gender = Gender.FEMALE;
        }
        this.friends = friends;
        this.habits = new HashSet<>();
    }

    public String getUsername() {
        return this.username;
    }

    public List<String> getFriends() {
        return this.friends;
    }

    public Set<Habit> getHabits() {
        return this.habits;
    }

    public Gender getGender() {
        return this.gender;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void addFriend(String u) {
        friends.add(u);
    }

    public void removeFriend(User u) {
        friends.remove(u);
    }

    public void addHabit(Habit h) {
        habits.add(h);
    }

    public void removeHabit(Habit h) {
        habits.remove(h);
    }

    public void setHabits(Set<Habit> habits) {
        this.habits = habits;
    }

    public String genderToString(Gender g) {
        if (g == Gender.MALE) {
            return "M";
        }
        return "F";
    }

    public Gender stringToGender(String g) {
        if (g.equals("M")) {
            return Gender.MALE;
        }
        return Gender.FEMALE;
    }

    public Habit findHabitbyId(String id) {
        for (Habit h : habits) {
            if (h.getId().equals(id)) {
                return h;
            }
        }
        return null;
    }

}
