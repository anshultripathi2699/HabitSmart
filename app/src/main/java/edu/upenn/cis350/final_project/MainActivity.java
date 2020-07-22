package edu.upenn.cis350.final_project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.widget.*;
import java.util.*;
import android.view.*;
import android.content.Intent;
import java.util.Random;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    public static final int FRIENDS_ID = 1;
    private RemoteDataSource dataSource;
    private User currentUser;
    private Adapter adapter;
    private List<Habit> habits;
    private RecyclerView recyclerView;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int rand = (int)(Math.random() * 5) + 1;
        ImageView iv = (ImageView)findViewById(R.id.cactusImageView);
        switch (rand){
            case 1: iv.setImageResource(R.drawable.icon1);
                break;
            case 2:  iv.setImageResource(R.drawable.icon2);
                break;
            case 3:  iv.setImageResource(R.drawable.icon3);
                break;
            case 4: iv.setImageResource(R.drawable.icon4);
                break;
            case 5: iv.setImageResource(R.drawable.icon5);
                break;
            default: iv.setImageResource(R.drawable.joker);
                break;
        }
        setupPage();

        username = getIntent().getStringExtra("username");
        //String username = "leahk";
        dataSource = new RemoteDataSource();
        currentUser = dataSource.getUser(username);
        getData();

        setupRecycler();
    }

    private void setupPage() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HabitSmart");

        // set page title "Your Habits"
        TextView subtitle = findViewById(R.id.subtitleTextView);
        subtitle.setText("Your Habits");

        // set button
        Button button = findViewById(R.id.feedButton);
        button.setText("Feed");
    }

    private void getData() {
        currentUser.setHabits(dataSource.getHabits(currentUser.getUsername()));
        habits = new ArrayList<>();
        habits.addAll(currentUser.getHabits());

        // sort habits by priority
        Collections.sort(habits, new Comparator<Habit>() {
            @Override
            public int compare(Habit h1, Habit h2) {
                int p1 = h1.getPriority();
                int p2 = h2.getPriority();
                if (p1 < p2) {
                    return 1;
                } else if (p1 > p2) {
                    return -1;
                }  else {
                    return 0;
                }
            }
        });
    }

    private void setupRecycler() {
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new Adapter(habits, FeedType.MY_HABITS);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }

    public void onFriendActivityButtonClick(View v) {
        Intent i = new Intent(this, FriendsActivity.class);
        i.putStringArrayListExtra("FRIENDS", (ArrayList<String>) currentUser.getFriends());
        i.putExtra("USER", currentUser.getUsername());
        startActivityForResult(i, FRIENDS_ID);
    }

    public void onNewHabitButtonClick(View v) {
        Intent i = new Intent(this, NewHabitActivity.class);
        i.putExtra("USER", currentUser.getUsername());
        startActivityForResult(i, FRIENDS_ID);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getData();
        setupRecycler();
    }

    public void onEditButtonClick(View v) {
        Intent i = new Intent(this, UpdateHabitActivity.class);
        i.putExtra("USER", currentUser.getUsername());

        String goal = extractGoalFromView((TextView) findViewById(R.id.goal));
        System.out.println("goal after: " + goal);
        String habitId = null;
        for (Habit h : habits) {
            if (h.getGoal().equals(goal)) {
                habitId = h.getId();
            }
        }
        if (habitId == null) {
            return;
        }

        i.putExtra("HABIT_ID", habitId);
        startActivityForResult(i, FRIENDS_ID);
    }

    public String extractGoalFromView(TextView goalView) {
        String goal = goalView.getText().toString();
        System.out.println("goal before: " + goal);
        goal = goal.substring(0, goal.length() - 54);
        int index = goal.length() - 1;
        while (goal.charAt(index) != ' ') {
            index--;
        }
        goal = goal.substring(0, index);
        return goal;
    }

    public void onFindPeopleButtonClick(View v){
        System.out.println("Find friends");
        Intent i = new Intent(this, FindPeopleActivity.class);
        i.putExtra("username", username);
        startActivityForResult(i, 1);
    }

    public void onEditUserButtonClick(View v){
        System.out.println("Update user details");
        Intent i = new Intent(this, EditUserActivity.class);
        i.putExtra("username", username);
        startActivityForResult(i, 1);
    }
}
