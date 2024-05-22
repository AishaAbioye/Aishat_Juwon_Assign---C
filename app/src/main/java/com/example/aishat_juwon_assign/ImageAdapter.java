package com.example.aishat_juwon_assign;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context context;
    private int[] images;

    public ImageAdapter(Context context, int[] images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageView imageView = new ImageView(context);
        // Set up the initial layout parameters for the ImageView
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
        imageView.setAdjustViewBounds(true);
        return new ImageViewHolder(imageView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.imageView.setImageResource(images[position]);
        // Update layout parameters if needed when binding
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.imageView.getLayoutParams();
        layoutParams.setMargins(4, 4, 4, 4); // Adjust margins as necessary
        holder.imageView.setLayoutParams(layoutParams);
        holder.imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        holder.imageView.setMaxHeight(70); // Max height of the image
        holder.imageView.setMaxWidth(70); // Max width of the image
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ImageViewHolder(ImageView itemView) {
            super(itemView);
            imageView = itemView;
        }
    }
}
