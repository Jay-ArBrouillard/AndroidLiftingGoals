package liftinggoals.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftinggoals.R;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.models.ExerciseModel;

public class ProgressAdapter extends RecyclerView.Adapter<ProgressAdapter.ExerciseViewHolder> implements Filterable {
    private ArrayList<ExerciseModel> exerciseList = new ArrayList<>();
    private ArrayList<ExerciseModel> exerciseListFull;
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

    public ProgressAdapter(ArrayList<ExerciseModel> exerciseList)
    {
        this.exerciseList = exerciseList;
        exerciseListFull = new ArrayList<>(exerciseList);
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
        ExerciseModel currentItem = exerciseList.get(position);

        holder.imageView.setImageResource(R.drawable.ic_keyboard_arrow_right_white_24dp);
        holder.textView.setText(currentItem.getExerciseName());
    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    @Override
    public Filter getFilter() {
        return progressFilter;
    }

    private Filter progressFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ExerciseModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(exerciseListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ExerciseModel item : exerciseListFull)
                {
                    if (item.getExerciseName().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            exerciseList.clear();
            exerciseList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
