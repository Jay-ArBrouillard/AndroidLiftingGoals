package com.example.liftinggoals;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class RoutineAdapter extends RecyclerView.Adapter<RoutineAdapter.RoutineViewHolder> implements Filterable {
    private ArrayList<RoutineModel> routineList;
    private ArrayList<RoutineModel> routineListFull; //Need a copy for the search function
    private OnItemClickListener listener;
    private int removedPosition = 0;
    private RoutineModel removedItem;

    public RoutineAdapter (ArrayList<RoutineModel> routineList) {
        this.routineList = routineList;
        routineListFull = new ArrayList<>(routineList);
    }

    public RoutineModel getItem(int position) {
        return routineList.get(position);
    }

    public void delete(RecyclerView.ViewHolder viewHolder, final int position) {
        removedPosition = position;
        removedItem = getItem(position);

        routineList.remove(position);
        routineListFull = new ArrayList<>(routineList);
        notifyItemRemoved(position);

        Snackbar.make(viewHolder.itemView, removedItem.getRoutineName() + " deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                routineList.add(removedPosition, removedItem);
                routineListFull = new ArrayList<>(routineList);
                notifyItemInserted(removedPosition);
            }
        }).show();

    }

    @NonNull
    @Override
    public RoutineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.workout_item, parent, false);
        RoutineViewHolder rvh = new RoutineViewHolder(view, listener);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RoutineViewHolder holder, int position) {
        RoutineModel currentItem = routineList.get(position);

        holder.routineName.setText(currentItem.getRoutineName());
        holder.lastPerformed.setText(currentItem.getRoutineDescription());
    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }

    @Override
    public Filter getFilter() {
        return routineFilter;
    }

    private Filter routineFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<RoutineModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {   //search field is null
                filteredList.addAll(routineListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (RoutineModel item : routineListFull) {
                    if (item.getRoutineName().toLowerCase().contains(filterPattern)) {
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
            ArrayList<RoutineModel> tempList = (ArrayList<RoutineModel>) results.values;
            routineList.clear();
            routineList.addAll(tempList);
            notifyDataSetChanged();
        }
    };

    //Inner class
    public static class RoutineViewHolder extends RecyclerView.ViewHolder {
        public ImageView folderIcon;
        public ImageView editIcon;
        public TextView routineName;
        public TextView lastPerformed;

        public RoutineViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            folderIcon = itemView.findViewById(R.id.dumbbell_icon);
            editIcon = itemView.findViewById(R.id.edit_icon);
            routineName = itemView.findViewById(R.id.title_text_view);
            lastPerformed = itemView.findViewById(R.id.description_text_view);


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
