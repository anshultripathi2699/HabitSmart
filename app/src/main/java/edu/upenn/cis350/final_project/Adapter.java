package edu.upenn.cis350.final_project;

import androidx.recyclerview.widget.RecyclerView;

import android.view.*;
import android.view.LayoutInflater;
import android.widget.*;
import java.util.*;

enum FeedType {
    MY_HABITS, FRIEND_HABITS
}

public class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder> {
    private FeedType feedType;
    private List<Habit> habits;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public ImageView icon;
        public TextView goal;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            icon = view.findViewById(R.id.icon);
            goal = view.findViewById(R.id.goal);
        }
    }

    public Adapter(List<Habit> habits, FeedType feedType) {
        this.habits = habits;
        this.feedType = feedType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.feed_item, parent, false);

        return new MyViewHolder((itemView));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Habit habit = habits.get(position);
        holder.name.setText(generateNameString(habit));
        holder.goal.setText(generateGoalString(habit));
        setIcon(holder.icon, habit);
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }

    private String generateNameString(Habit habit) {
        String who = "";
        if (this.feedType == FeedType.MY_HABITS) {
            who =  "You";
        } else if (this.feedType == FeedType.FRIEND_HABITS) {
            who = habit.getUser();
        }
        return who + " set a goal to...";
    }

    private String generateGoalString(Habit h) {
        String who = "";
        if (this.feedType == FeedType.MY_HABITS) {
            who = "You";
        } else if (this.feedType == FeedType.FRIEND_HABITS) {
            who = "They";
        }
        String str = h.getGoal() + " " + h.getTimesPerWeekGoal() + " times per week.\n" +
                who + "'ve achieved it " + h.getTimesThisWeek() + " times this week!";
        return str;
    }

    private void setIcon(ImageView icon, Habit h) {
        if (h.getCategory() == Category.MENTAL_HEALTH) {
            icon.setImageResource(R.drawable.mental);
        } else if (h.getCategory() == Category.FITNESS) {
            icon.setImageResource(R.drawable.fitness);
        } else if (h.getCategory() == Category.DIET) {
            icon.setImageResource(R.drawable.diet);
        } else if (h.getCategory() == Category.SLEEP) {
            icon.setImageResource(R.drawable.sleep);
        } else if (h.getCategory() == Category.SOCIAL) {
            icon.setImageResource(R.drawable.social);
        } else if (h.getCategory() == Category.OTHER) {
            icon.setImageResource(R.drawable.love);
        }
    }
}
