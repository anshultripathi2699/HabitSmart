package edu.upenn.cis350.final_project;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateHabitActivity extends AppCompatActivity implements OnItemSelectedListener {
    Habit habit;
    RemoteDataSource ds;
    String username;
    String habitId;
    User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_habit);

        getData();

        setupUI();
    }

    public void getData() {
        ds = new RemoteDataSource();
        username = getIntent().getStringExtra("USER");
        habitId = getIntent().getStringExtra("HABIT_ID");
        currentUser = ds.getUser(username);
        currentUser.setHabits(ds.getHabits(currentUser.getUsername()));

        habit = currentUser.findHabitbyId(habitId);
        if (habit == null) {
            System.out.println("null habit");
            return ;
        }
        System.out.println("habit not null");
    }

    public void setupUI() {
        setCategorySpinner();
        setPrioritySpinner();

        EditText goalView = findViewById(R.id.goalEditText);
        goalView.setText(habit.getGoal());

        EditText timesPerWeek = findViewById(R.id.timesPerWeekEditText);
        timesPerWeek.setText(String.valueOf(habit.getTimesPerWeekGoal()));

        EditText timesThisWeek = findViewById(R.id.timesThisWeekEditText);
        timesThisWeek.setText(String.valueOf(habit.getTimesThisWeek()));
    }

    private void setCategorySpinner() {
        Spinner spinner = findViewById(R.id.categorySpinner);
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(categoryAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setPrioritySpinner() {
        Spinner spinner = findViewById(R.id.prioritySpinner);
        ArrayAdapter<CharSequence> priorityAdapter= ArrayAdapter.createFromResource(this,
                R.array.priority_array, android.R.layout.simple_spinner_item);
        priorityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(priorityAdapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.categorySpinner) {
            Category c = habit.stringToCategory((String) parent.getItemAtPosition(pos));
            habit.setCategory(c);
        } else if (spinner.getId() == R.id.prioritySpinner) {
            int priority = Integer.parseInt((String) parent.getItemAtPosition(pos));
            habit.setPriority(priority);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // do nothing, keep habit data as is
    }

    public void onSubmitButtonClick(View v){
        EditText goalView = findViewById(R.id.goalEditText);
        String goal = goalView.getText().toString();
        if (goal.equalsIgnoreCase("Enter your goal here.") || goal.equals("")) {
            String toast = "Please enter a goal.";
            Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
            return;
        }
        habit.setGoal(goal);

        // get times per week goal
        int timesPerWeekGoal = getTimesPerWeekGoal();
        if (timesPerWeekGoal< 1) {
            // toast for invalid times per week goal
            String toast = "Invalid time per week goal.\nPlease enter a positive number";
            Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
            return;
        }
        habit.setTimesPerWeekGoal(timesPerWeekGoal);

        // get times this week
        int timesThisWeek = getTimesThisWeek();
        if (timesThisWeek < 1) {
            // toast for invalid times per week goal
            String toast = "Invalid time times this week.\nPlease enter a positive number";
            Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();
            return;
        }
        habit.setTimesThisWeek(timesThisWeek);

        // update habit in db
        ds.updateHabit(habit);
        String toast = "Habit updated!";
        Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_LONG).show();

    }

    private int getTimesPerWeekGoal() {
        EditText timesView = findViewById(R.id.timesPerWeekEditText);
        try {
            int num = Integer.parseInt(timesView.getText().toString());
            if (num < 1) {
                return -1;
            }
            return num;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private int getTimesThisWeek() {
        EditText timesView = findViewById(R.id.timesThisWeekEditText);
        try {
            int num = Integer.parseInt(timesView.getText().toString());
            if (num < 1) {
                return -1;
            }
            return num;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void onCancelButtonClick(View v) {
        finish();
    }
}
