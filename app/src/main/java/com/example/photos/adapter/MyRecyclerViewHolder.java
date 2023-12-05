package com.example.photos.adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photos.R;

public class MyRecyclerViewHolder extends RecyclerView.ViewHolder {

    TextView albumName;
    ImageView albumFirstImages;
    CardView albumCardView;

    public MyRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        albumName = itemView.findViewById(R.id.album_name_textView);
        albumFirstImages = itemView.findViewById(R.id.album_first_images);
        albumCardView = itemView.findViewById(R.id.albumCardViewId);

    }
}
