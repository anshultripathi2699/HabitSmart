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


public class NewHabitActivity extends AppCompatActivity implements OnItemSelectedListener {
    User currentUser;
    String user;
    RemoteDataSource ds;
    Habit habit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_habit);

        ds = new RemoteDataSource();

        setCategorySpinner();
        setPrioritySpinner();
        user = getIntent().getStringExtra("USER");
        habit = new Habit();
        habit.setUser(user);

        currentUser = ds.getUser(user);
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
        Spinner spinner = (Spinner) parent;
        if (spinner.getId() == R.id.categorySpinner) {
            habit.setCategory(Category.MENTAL_HEALTH);
        } else if (spinner.getId() == R.id.prioritySpinner) {
            habit.setPriority(1);
        }
    }

    public void onSubmitButtonClick(View v) {
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

        // add habit to db
        ds.addHabit(habit);
        String toast = "Habit added!";
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

    public void onCancelButtonClick(View v) {
        finish();
    }

}
