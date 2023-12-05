package com.example.photos.activity;

import static com.example.photos.databse.PhotoDatabase.albums;
import static com.example.photos.databse.PhotoDatabase.findAlbumByName;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photos.adapter.PhotoAdapter;
import com.example.photos.R;
import com.example.photos.databse.PhotoDatabase;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AlbumView extends AppCompatActivity {

    private Album selectedAlbum;

    PhotoAdapter photoAdapter;

    private Context context;

    private static final int REQUEST_CODE_GALLERY = 1;

    private Button viewSlideShow, addPhoto, renameAlbum, deleteAlbum;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_view);

        // Set up the back button
        ImageView backButton = findViewById(R.id.backImageViewId);
        TextView backButtonText = findViewById(R.id.backTextViewId);
        backButton.setOnClickListener(view -> onBackPressed());
        backButtonText.setOnClickListener(view -> onBackPressed());
        Intent intent = getIntent();
        if (intent != null) {
            selectedAlbum = (Album) intent.getSerializableExtra("album");
            // Check if selectedAlbum is not null and has photos
            if (selectedAlbum != null && selectedAlbum.getPhotos() != null && !selectedAlbum.getPhotos().isEmpty()) {
                List<Photo> albumPhotos = selectedAlbum.getPhotos();
                // Pass both the context, album, and photos to the PhotoAdapter
                PhotoAdapter photoAdapter = new PhotoAdapter(this, albumPhotos, selectedAlbum);
                // Set up the RecyclerView and PhotoAdapter
                RecyclerView photoRecyclerView = findViewById(R.id.photoRecyclerView);
                photoRecyclerView.setAdapter(photoAdapter);

                // Use a LinearLayoutManager or a GridLayoutManager with the desired number of columns
                photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            }
        }
/*
        // Initialize the adapter with an empty list initially
        photoAdapter = new PhotoAdapter(this, new ArrayList<>(), selectedAlbum);
        // Set up the back button
        ImageView backButton = findViewById(R.id.backImageViewId);
        TextView backButtonText = findViewById(R.id.backTextViewId);

        // all button initializes
        viewSlideShow = findViewById(R.id.viewSlide_album_button);
        addPhoto = findViewById(R.id.add_photo_button);
        renameAlbum = findViewById(R.id.rename_album_button);
        deleteAlbum = findViewById(R.id.delete_album_button);

        backButton.setOnClickListener(view -> onBackPressed());
        backButtonText.setOnClickListener(view -> onBackPressed());
        Intent intent = getIntent();
        selectedAlbum = (Album) intent.getSerializableExtra("album");
            // Check if selectedAlbum is not null and has photos
            // Inside your onCreate method
            if (selectedAlbum != null) {
                // Check if the selected album has photos
                if (selectedAlbum.getPhotos() != null && !selectedAlbum.getPhotos().isEmpty()) {
                    List<Photo> albumPhotos = selectedAlbum.getPhotos();
                    photoAdapter = new PhotoAdapter(this, albumPhotos, selectedAlbum);
                    RecyclerView photoRecyclerView = findViewById(R.id.photoRecyclerView);
                    photoRecyclerView.setAdapter(photoAdapter);
                    photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                } else {
                    // If the selected album doesn't have photos, create an empty adapter
                    photoAdapter = new PhotoAdapter(this, new ArrayList<>(), selectedAlbum);
                    RecyclerView photoRecyclerView = findViewById(R.id.photoRecyclerView);
                    photoRecyclerView.setAdapter(photoAdapter);
                    photoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                }
            }
            viewSlideShow.setOnClickListener(view->{
                Intent intent1 = new Intent(AlbumView.this, ViewSlideShow.class);
                intent1.putExtra("ALBUM_KEY", selectedAlbum);
                startActivity(intent1);
            });
            addPhoto.setOnClickListener(view->{
                // here I want to add photo from device gallery photo to selected album
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, REQUEST_CODE_GALLERY);
            });
            renameAlbum.setOnClickListener(view->{
                // here I want to rename selected album take album name from dialog
            });
            deleteAlbum.setOnClickListener(view->{
                // here i want to delete selected album
            });
            // Restore the selected album from the instance state if available
            if (savedInstanceState != null) {
                selectedAlbum = (Album) savedInstanceState.getSerializable("selectedAlbum");
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            // User has selected a photo from the gallery
            Uri selectedImageUri = data.getData();

            // Check if selectedAlbum is not null
            if (selectedAlbum != null) {
                // Create a new photo from the URI
                Photo newPhoto = createPhotoFromUri(selectedImageUri);
                if (newPhoto != null) {
                    // Set the associated album for the new photo
                    newPhoto.setAssociatedAlbum(selectedAlbum);
                    // Add the photo to the selected album
                    selectedAlbum.addPhoto(newPhoto);
                    // Update the adapter with the new photo
                    int position = selectedAlbum.getPhotos().indexOf(newPhoto);
                    photoAdapter.notifyItemInserted(position);
                    // Update the database with the new photo
                    PhotoDatabase.addPhoto(newPhoto, selectedAlbum);
                    // Update selectedAlbum to reflect the changes
                    selectedAlbum = findAlbumByName(selectedAlbum.getName());
                    setResult(RESULT_OK);
                } else {
                    // Handle the case when creating a Photo object fails
                    Toast.makeText(this, "Error: Unable to create a Photo object", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }




    // Method to create a Photo object from the URI
    private Photo createPhotoFromUri(Uri uri) {
        if (selectedAlbum != null) {
            Photo newPhoto = new Photo(uri);
            newPhoto.setAssociatedAlbum(selectedAlbum);
            Log.d("PhotoCreation", "New Photo: " + newPhoto.toString());  // Add logging
            return newPhoto;
        } else {
            // Handle the case when selectedAlbum is null
            Log.e("PhotoCreation", "Error: No album selected");
            return null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save the selected album to the instance state
        outState.putSerializable("selectedAlbum", selectedAlbum);
    }

    // This method ensures that the RecyclerView is updated with the current album data



    // Method to add photos to the selected album
    private void addPhotosToAlbum(List<Photo> selectedPhotos) {
        if (selectedAlbum != null) {
            selectedAlbum.getPhotos().addAll(selectedPhotos);
        }
    }

    // Method to retrieve added photos from the selected album
    private List<Photo> getAddedPhotos() {
        if (selectedAlbum != null) {
            return selectedAlbum.getPhotos();
        }
        return Collections.emptyList(); // Return an empty list if no album is selected

 */
    }

}
