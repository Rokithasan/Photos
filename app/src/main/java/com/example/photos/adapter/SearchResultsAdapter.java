package com.example.photos.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photos.R;
import com.example.photos.model.Photo;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {

    private List<Photo> searchResults;

    public void setSearchResults(List<Photo> searchResults) {
        this.searchResults = searchResults;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.for_add_photos, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo photo = searchResults.get(position);
        holder.bind(photo);
    }

    @Override
    public int getItemCount() {
        return searchResults != null ? searchResults.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView photo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            photo = itemView.findViewById(R.id.photoImageView_for_add); // Replace with the actual ID of your ImageView in the item layout
        }
        public void bind(Photo photoItem) {
            // Assuming you have a method like getImageResourceId() in your Photo class
            int imageResourceId = photoItem.getImageResourceId();
            // Set the image resource to the ImageView
            photo.setImageResource(imageResourceId);
        }
    }

}

