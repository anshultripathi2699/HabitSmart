package edu.upenn.cis350.final_project;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.*;
import java.util.*;

public class FriendsActivity extends AppCompatActivity {
    public static final int MAIN_ID = 1;
    private RemoteDataSource dataSource;
    private String user;
    private Adapter adapter;
    private List<Habit> habits;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupPage();
        getData();
        setupRecycler();
    }

    private void setupPage() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HabitSmart");

        // set page title "Your Friends' Activity"
        TextView subtitle = findViewById(R.id.subtitleTextView);
        subtitle.setText("Your Friends' Activity");

        // set button
        Button button = findViewById(R.id.feedButton);
        button.setText("Habits");
    }

    private void getData() {
        dataSource = new RemoteDataSource();

        List<String> friends = getIntent().getStringArrayListExtra("FRIENDS");
        habits = getFriendsHabits(friends);

        user = getIntent().getStringExtra("USER");
    }

    private List<Habit> getFriendsHabits(List<String> friends) {
        List<Habit> habits = new ArrayList<>();
        for (String friend : friends) {
            Set<Habit> set = dataSource.getHabits(friend);
            habits.addAll(set);
        }

        Collections.sort(habits, new Comparator<Habit>() {
            @Override
            public int compare(Habit h1, Habit h2) {
                return -1 * h1.getLastUpdated().compareTo(h2.getLastUpdated());
            }
        });

        return habits;
    }

    private void setupRecycler() {
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new Adapter(habits, FeedType.FRIEND_HABITS);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
    }


    public void onFriendActivityButtonClick(View v) {
        finish();
    }

    public void onNewHabitButtonClick(View v) {
        Intent i = new Intent(this, NewHabitActivity.class);
        i.putExtra("USER", user);
        startActivityForResult(i, MAIN_ID);
    }

    public void onEditButtonClick(View v) {
        // do nothing
    }

}
