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

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ExerciseViewHolder> {
    private ArrayList<ExerciseModel> exerciseItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public ExerciseViewHolder(@NonNull final View itemView, final OnItemClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.volume_group_right_arrow);
            textView = itemView.findViewById(R.id.volume_group_text_view);

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

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemView.performClick();
                }
            });
        }
    }

    public ProgressAdapter(ArrayList<ExerciseModel> exerciseItems)
    {
        this.exerciseItems = exerciseItems;
    }

    @NonNull
    @Override
    public ProgressAdapter.ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
        ProgressAdapter.ExerciseViewHolder evh = new ProgressAdapter.ExerciseViewHolder(v, listener);
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
