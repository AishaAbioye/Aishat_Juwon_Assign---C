package com.example.aishat_juwon_assign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ComboAdapter extends RecyclerView.Adapter<ComboAdapter.ComboViewHolder> {
    private List<ComboItem> comboItems;
    private ComboInteractionListener listener;
    private Context context;
    private int[][] imagesPerCombo;

    public ComboAdapter(Context context, List<ComboItem> comboItems, int[][] imagesPerCombo, ComboInteractionListener listener) {
        this.context = context;
        this.comboItems = comboItems;
        this.imagesPerCombo = imagesPerCombo;
        this.listener = listener;
    }

    @Override
    public ComboViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_combo, parent, false);
        return new ComboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ComboViewHolder holder, int position) {
        if (comboItems != null && position < comboItems.size()) {
            ComboItem item = comboItems.get(position);
            holder.tvComboName.setText(item.getName());

            // Set background color of rvComboImages based on the result
            if (item.isAttempted()) {
                holder.rvComboImages.setBackgroundColor(context.getResources().getColor(
                        item.getResult() ? R.color.green : R.color.red
                ));
            } else {
                holder.rvComboImages.setBackgroundColor(context.getResources().getColor(R.color.defaultBackground));
            }

            holder.itemView.setOnClickListener(v -> listener.onComboSelected(position, item));

            // Set up the RecyclerView for images within each combo item
            if (imagesPerCombo != null && position < imagesPerCombo.length && imagesPerCombo[position] != null) {
                holder.rvComboImages.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                ImageAdapter imageAdapter = new ImageAdapter(context, imagesPerCombo[position]);
                holder.rvComboImages.setAdapter(imageAdapter);
            } else {
                holder.rvComboImages.setAdapter(null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return comboItems != null ? comboItems.size() : 0;
    }

    public static class ComboViewHolder extends RecyclerView.ViewHolder {
        TextView tvComboName;
        RecyclerView rvComboImages;

        public ComboViewHolder(View itemView) {
            super(itemView);
            tvComboName = itemView.findViewById(R.id.tvComboName);
            rvComboImages = itemView.findViewById(R.id.rvComboImages);
        }
    }
}
