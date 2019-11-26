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

import liftinggoals.models.VolumeGroupModel;

public class VolumeAdapter extends RecyclerView.Adapter<VolumeAdapter.VolumeViewHolder> implements Filterable {
    private ArrayList<VolumeGroupModel> volumeGroupList;
    private ArrayList<VolumeGroupModel> volumeGroupListFull;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.listener = listener;
    }

    public static class VolumeViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public VolumeViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
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
        }
    }

    public VolumeAdapter (ArrayList<VolumeGroupModel> volumeGroupList)
    {
        this.volumeGroupList = volumeGroupList;
        volumeGroupListFull = new ArrayList<>(volumeGroupList);
    }

    @NonNull
    @Override
    public VolumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
        VolumeViewHolder vvh = new VolumeViewHolder(v, listener);
        return vvh;
    }

    @Override
    public void onBindViewHolder(@NonNull VolumeViewHolder holder, int position) {
        VolumeGroupModel currentItem = volumeGroupList.get(position);

        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.textView.setText(currentItem.getVolumeGroup());
    }

    @Override
    public int getItemCount() {
        return volumeGroupList.size();
    }

    @Override
    public Filter getFilter() {
        return volumeFilter;
    }

    private Filter volumeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<VolumeGroupModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(volumeGroupListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (VolumeGroupModel item : volumeGroupListFull)
                {
                    if (item.getVolumeGroup().toLowerCase().contains(filterPattern))
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
            volumeGroupList.clear();
            volumeGroupList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
