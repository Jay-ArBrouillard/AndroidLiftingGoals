package liftinggoals.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftinggoals.R;

import java.util.ArrayList;

import liftinggoals.classes.HistoryItem;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private ArrayList<HistoryItem> historyItems;

    public static class HistoryViewHolder extends RecyclerView.ViewHolder {
        public TextView workoutName;
        public TextView exercises;
        public TextView date;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            workoutName = itemView.findViewById(R.id.history_item_workout_name);
            exercises = itemView.findViewById(R.id.history_item_exercises);
            date = itemView.findViewById(R.id.history_item_date);

        }
    }

    public HistoryAdapter(ArrayList<HistoryItem> historyItems)
    {
        this.historyItems = historyItems;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        HistoryViewHolder hvh = new HistoryViewHolder(v);
        return hvh;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        HistoryItem currentItem = historyItems.get(position);

        holder.workoutName.setText(currentItem.getWorkoutName());
        holder.exercises.setText(currentItem.getWorkoutName());
        holder.date.setText(currentItem.getDate());
    }

    @Override
    public int getItemCount() {
        return historyItems.size();
    }

}
