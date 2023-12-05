package com.example.photos.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photos.R;
import com.example.photos.activity.AlbumView;
import com.example.photos.activity.MainActivity;
import com.example.photos.databse.PhotoDatabase;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;

import java.util.List;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {
    private int selectedPosition = RecyclerView.NO_POSITION;
    private GestureDetector gestureDetector;
    private RecyclerView recyclerView;
    private static final int REQUEST_CODE_RENAME = 1;
    private static final int REQUEST_CODE_DELETE = 2;
    private Context context;
    private List<Album> albums;

    PhotoDatabase database ;
    public MyRecyclerViewAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                int position = getAdapterPositionFromView(e.getX(), e.getY());
                if (position != RecyclerView.NO_POSITION) {
                    Album selectedAlbum = albums.get(position);
                    Intent intent = new Intent(context, AlbumView.class);
                    intent.putExtra("album", selectedAlbum);
                    context.startActivity(intent);
                    return true;
                }
                return false;
            }
        });
    }

    public MyRecyclerViewAdapter(MainActivity context, List<Photo> photos, Album newAlbum) {
    }
    private int getAdapterPositionFromView(float x, float y) {
        View childView = recyclerView.findChildViewUnder(x, y);
        if (childView != null) {
            return recyclerView.getChildAdapterPosition(childView);
        }
        return RecyclerView.NO_POSITION;
    }
    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
    public int getSelectedAlbumIndex() {
        return selectedPosition;
    }
    @NonNull
    @Override
    public MyRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyRecyclerViewHolder(LayoutInflater.from(context).inflate(R.layout.album_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerViewHolder holder, int position) {

        Album album = PhotoDatabase.getAlbums().get(position);
        holder.albumName.setText(album.getName());
        List<Photo> photos = album.getPhotos();
        if (!photos.isEmpty()) {
            // If there are photos in the album, display the first one
            Photo firstPhoto = photos.get(0);
            if (firstPhoto.getUri() != null) {
                // Load image from URI
                loadUriImage(holder.albumFirstImages, firstPhoto.getUri());
            } else if (firstPhoto.getImageResourceId() != 0) {
                // Load image from drawable resource
                holder.albumFirstImages.setImageResource(firstPhoto.getImageResourceId());
            }
        } else {
            // If there are no photos in the album, you might want to set a default image or leave it empty
            holder.albumFirstImages.setImageDrawable(null);
        }
        // open album card view
        holder.albumCardView.setOnClickListener(view -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                selectedPosition = adapterPosition;  // Update selected position here
                Album selectedAlbum = albums.get(adapterPosition);
                Intent intent = new Intent(context, AlbumView.class);
                Toast.makeText(view.getContext(), "Clicked " + adapterPosition, Toast.LENGTH_SHORT).show();
                // Pass the selected album to AlbumView activity
                intent.putExtra("album", selectedAlbum);
                view.getContext().startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void renameSelectedAlbum(String newAlbumName) {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            Album selectedAlbum = albums.get(selectedPosition);
            selectedAlbum.setName(newAlbumName);
            notifyItemChanged(selectedPosition);
        }
    }
    public void deleteSelectedAlbum() {
        if (selectedPosition != RecyclerView.NO_POSITION) {
            albums.remove(selectedPosition);
            notifyItemRemoved(selectedPosition);
            selectedPosition = RecyclerView.NO_POSITION;
        }
    }
    public void setAlbums(List<Album> albums) {
        this.albums = albums;
        notifyDataSetChanged(); // Notify the adapter that the dataset has changed
    }
    private void loadUriImage(ImageView imageView, Uri uri) {
        // Use a library like Glide or Picasso to load images efficiently
        // For example, using Glide:
        Glide.with(context)
                .load(uri)
                .into(imageView);
    }


}
