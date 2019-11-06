package liftinggoals.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftinggoals.R;

import java.util.ArrayList;

import liftinggoals.classes.ExerciseModel;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private ArrayList<ExerciseModel> exerciseItems;

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.volume_group_right_arrow);
            textView = itemView.findViewById(R.id.volume_group_text_view);
        }
    }

    public ExerciseAdapter (ArrayList<ExerciseModel> exerciseItems)
    {
        this.exerciseItems = exerciseItems;
    }

    @NonNull
    @Override
    public ExerciseAdapter.ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
        ExerciseAdapter.ExerciseViewHolder evh = new ExerciseAdapter.ExerciseViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        ExerciseModel currentItem = exerciseItems.get(position);

        holder.imageView.setImageResource(R.drawable.ic_keyboard_arrow_right_white_24dp);
        holder.textView.setText(currentItem.getExerciseName());
    }

    @Override
    public int getItemCount() {
        return exerciseItems.size();
    }
}
