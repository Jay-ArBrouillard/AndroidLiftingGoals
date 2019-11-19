package liftinggoals.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.liftinggoals.R;
import java.util.ArrayList;
import liftinggoals.classes.RecordModel;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.RecordViewHolder> {
    private ArrayList<RecordModel> recordModels;

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView repMax;
        public TextView weight;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.rep_record_item_date);
            repMax = itemView.findViewById(R.id.rep_record_item_rep_max);
            weight = itemView.findViewById(R.id.rep_record_item_rep_weight_value);
        }
    }

    public RecordsAdapter(ArrayList<RecordModel> recordModels)
    {
        this.recordModels = recordModels;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rep_record_item, parent, false);
        RecordViewHolder rvh = new RecordViewHolder(v);
        return rvh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        RecordModel currentItem = recordModels.get(position);
        holder.date.setText(currentItem.getDate());
        holder.repMax.setText(String.valueOf(currentItem.getRepsPerformed()));
        holder.weight.setText(String.valueOf(currentItem.getIntensity()).trim());
    }

    @Override
    public int getItemCount() {
        return recordModels.size();
    }
}
