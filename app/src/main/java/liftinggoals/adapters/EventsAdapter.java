package liftinggoals.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liftinggoals.R;

import java.util.ArrayList;

import liftinggoals.calendar.Event;

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {
    private ArrayList<Event> eventsList;

    public static class EventsViewHolder extends RecyclerView.ViewHolder {
        public TextView event;
        public TextView exercises;
        public TextView date;

        public EventsViewHolder(@NonNull View itemView) {
            super(itemView);

            event = itemView.findViewById(R.id.event_name);
            exercises = itemView.findViewById(R.id.event_exercises);
            date = itemView.findViewById(R.id.event_date);
        }
    }

    public EventsAdapter(ArrayList<Event> eventsList)
    {
        this.eventsList = eventsList;
    }

    @NonNull
    @Override
    public EventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        EventsAdapter.EventsViewHolder evh = new EventsAdapter.EventsViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull EventsViewHolder holder, int position) {
        Event currentItem = eventsList.get(position);

        holder.event.setText(currentItem.getEVENT());
        holder.exercises.setText(currentItem.getExercises());
        holder.date.setText(currentItem.getFULL_DATE());
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }
}
