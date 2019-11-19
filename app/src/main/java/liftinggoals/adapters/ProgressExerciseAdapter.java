package liftinggoals.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftinggoals.R;

import java.util.ArrayList;

import liftinggoals.classes.ProgressExerciseModel;

public class ProgressExerciseAdapter extends RecyclerView.Adapter<ProgressExerciseAdapter.ProgressExerciseViewHolder> {
    private ArrayList<ProgressExerciseModel> progressExerciseModels;
    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public static class ProgressExerciseViewHolder extends RecyclerView.ViewHolder {
        public TextView index;
        public TextView exerciseName;
        public TextView specs;
        public TextView time;

        public ProgressExerciseViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            index = itemView.findViewById(R.id.activity_progress_exercise_index);
            exerciseName = itemView.findViewById(R.id.activity_progress_exercise_name);
            specs = itemView.findViewById(R.id.activity_progress_exercise_reps_sets);
            time = itemView.findViewById(R.id.activity_progress_exercise_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ProgressExerciseAdapter(ArrayList<ProgressExerciseModel> progressExerciseModels)
    {
        this.progressExerciseModels = progressExerciseModels;
    }

    @NonNull
    @Override
    public ProgressExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_exercise_item, parent, false);
        ProgressExerciseViewHolder pevh = new ProgressExerciseViewHolder(v, listener);
        return pevh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProgressExerciseViewHolder holder, int position) {
        ProgressExerciseModel currentItem = progressExerciseModels.get(position);

        holder.index.setText(currentItem.getIndex());
        holder.exerciseName.setText(currentItem.getExerciseName());
        holder.specs.setText(currentItem.getSpecs());
        holder.time.setText(currentItem.getTime());
    }

    @Override
    public int getItemCount() {
        return progressExerciseModels.size();
    }



}
