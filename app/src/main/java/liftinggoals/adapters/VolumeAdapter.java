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

import liftinggoals.classes.VolumeGroupModel;

public class VolumeAdapter extends RecyclerView.Adapter<VolumeAdapter.VolumeViewHolder> {
    private ArrayList<VolumeGroupModel> volumeGroupModels;

    public static class VolumeViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;

        public VolumeViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.volume_group_right_arrow);
            textView = itemView.findViewById(R.id.volume_group_text_view);
        }
    }

    public VolumeAdapter (ArrayList<VolumeGroupModel> volumeGroupModels)
    {
        this.volumeGroupModels = volumeGroupModels;
    }

    @NonNull
    @Override
    public VolumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.progress_item, parent, false);
        VolumeViewHolder vvh = new VolumeViewHolder(v);
        return vvh;
    }

    @Override
    public void onBindViewHolder(@NonNull VolumeViewHolder holder, int position) {
        VolumeGroupModel currentItem = volumeGroupModels.get(position);

        holder.imageView.setImageResource(currentItem.getImageResource());
        holder.textView.setText(currentItem.getVolumeGroup());
    }

    @Override
    public int getItemCount() {
        return volumeGroupModels.size();
    }
}
