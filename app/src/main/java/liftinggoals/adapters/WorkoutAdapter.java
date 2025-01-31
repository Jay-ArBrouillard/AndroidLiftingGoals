package liftinggoals.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftinggoals.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import liftinggoals.models.WorkoutModel;

public class WorkoutAdapter extends RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder> {
    private ArrayList<WorkoutModel> workoutList;
    private ArrayList<WorkoutModel> workoutListFull; //Need a copy for the search function
    private WorkoutAdapter.OnItemClickListener listener;
    private int removedPosition = 0;
    private WorkoutModel removedItem;

    public WorkoutAdapter(ArrayList<WorkoutModel> workoutList) {
        if (workoutList == null)
        {
            this.workoutList = new ArrayList<>();
            workoutListFull = new ArrayList<>();
        }
        else {
            this.workoutList = workoutList;
            workoutListFull = new ArrayList<>(workoutList);
        }
    }

    public WorkoutModel getItem(int position) {
        return workoutList.get(position);
    }

    public void delete(RecyclerView.ViewHolder viewHolder, final int position) {
        workoutList.remove(position);
        workoutListFull = new ArrayList<>(workoutList);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        WorkoutViewHolder wvh = new WorkoutViewHolder(view, listener);
        return wvh;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutModel currentItem = workoutList.get(position);

        holder.workoutName.setText(currentItem.getWorkoutName());
        holder.numExercises.setText(Integer.toString(currentItem.getNumberExercises()));
    }

    @Override
    public int getItemCount() {
        return workoutList.size();
    }

    public Filter getFilter() {
        return workoutFilter;
    }

    private Filter workoutFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<WorkoutModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {   //search field is null
                filteredList.addAll(workoutListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (WorkoutModel item : workoutListFull) {
                    if (item.getWorkoutName().toLowerCase().contains(filterPattern)) {
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
            @SuppressWarnings("unchecked")
            ArrayList<WorkoutModel> tempList = (ArrayList<WorkoutModel>) results.values;
            workoutList.clear();
            workoutList.addAll(tempList);
            notifyDataSetChanged();
        }
    };


    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        public ImageView dumbbellIcon;
        public ImageView editIcon;
        public TextView workoutName;
        public TextView numExercises;

        public WorkoutViewHolder(@NonNull View itemView, final WorkoutAdapter.OnItemClickListener listener) {
            super(itemView);
            dumbbellIcon = itemView.findViewById(R.id.dumbbell_icon);
            editIcon = itemView.findViewById(R.id.edit_icon);
            workoutName = itemView.findViewById(R.id.title_text_view);
            numExercises = itemView.findViewById(R.id.workout_item_num_exercises_value);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            editIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemEdit(position);
                        }
                    }
                }
            });
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemEdit(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
