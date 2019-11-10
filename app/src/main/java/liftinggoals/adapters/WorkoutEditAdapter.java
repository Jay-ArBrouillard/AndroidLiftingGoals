package liftinggoals.adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftinggoals.R;

import java.util.ArrayList;

import liftinggoals.classes.WorkoutExerciseModel;


public class WorkoutEditAdapter extends RecyclerView.Adapter<WorkoutEditAdapter.WorkoutEditViewHolder> {
    private ArrayList<WorkoutExerciseModel> workoutExerciseItems;

    public static class WorkoutEditViewHolder extends RecyclerView.ViewHolder {
        public TextView letter;
        public TextView sets;
        public TextView reps;
        public TextView exerciseName;
        public TextView intensity;
        public ImageView imageResource;

        public WorkoutEditViewHolder(@NonNull View itemView) {
            super(itemView);
            letter = itemView.findViewById(R.id.workout_edit_item_letter);
            sets = itemView.findViewById(R.id.workout_edit_item_sets);
            reps = itemView.findViewById(R.id.workout_edit_item_reps_value);
            exerciseName = itemView.findViewById(R.id.workout_edit_item_exercise_name);
            intensity = itemView.findViewById(R.id.workout_edit_item_reps_intensity_value);
            imageResource = itemView.findViewById(R.id.workout_edit_item_right_arrow);
        }
    }

    public WorkoutEditAdapter (ArrayList<WorkoutExerciseModel> workoutExerciseItems)
    {
        this.workoutExerciseItems = workoutExerciseItems;
    }

    @NonNull
    @Override
    public WorkoutEditViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_edit_item, parent, false);
        WorkoutEditViewHolder wevh = new WorkoutEditViewHolder(v);
        return wevh;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutEditViewHolder holder, int position) {
        WorkoutExerciseModel currentItem = workoutExerciseItems.get(position);
        String letter = getLetterForNumber(position);
        String sets = handleSets(currentItem);
        String reps = handleReps(currentItem);

        holder.letter.setText(letter+".");
        holder.sets.setText(sets);
        holder.reps.setText(reps);
        holder.exerciseName.setText(currentItem.getExercise().getExerciseName());
        holder.imageResource.setImageResource(R.drawable.ic_keyboard_arrow_right_white_24dp);
    }

    @Override
    public int getItemCount() {
        return workoutExerciseItems.size();
    }

    private String handleSets(WorkoutExerciseModel item)
    {
        StringBuilder stringBuilder = new StringBuilder();
        int minSets = item.getMinimumSets();
        int maxSets = item.getMaximumSets();

        System.out.println("minSets: " + minSets);
        System.out.println("maxSets: " + maxSets);


        if (minSets == -1 && maxSets == -1) stringBuilder.append("");
        else if (minSets == -1 && maxSets != -1) stringBuilder.append(maxSets).append(" Sets");
        else if (minSets != -1 && maxSets == -1) stringBuilder.append(minSets).append("+ Sets");
        else stringBuilder.append(minSets).append("-").append(maxSets).append(" Sets");

        return stringBuilder.toString();
    }

    private String handleReps(WorkoutExerciseModel item)
    {
        StringBuilder stringBuilder = new StringBuilder();
        int minReps = item.getMinimumReps();
        int maxReps = item.getMaximumReps();
        if (minReps == -1 && maxReps == -1) stringBuilder.append("");
        else if (minReps == -1 && maxReps != -1) stringBuilder.append(maxReps).append(" Sets");
        else if (minReps != -1 && maxReps == -1) stringBuilder.append(minReps).append("+ Sets");
        else stringBuilder.append(minReps).append("-").append(maxReps).append(" Sets");

        return stringBuilder.toString();
    }

    private String getLetterForNumber (int i) {
        i++;
        return i > 0 && i < 27 ? String.valueOf((char)(i + 64)) : null;
    }
}
