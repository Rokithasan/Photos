package com.example.photos.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.photos.adapter.AddPhotoAdapter;
import com.example.photos.adapter.ImagePagerAdapter;
import com.example.photos.databse.PhotoDatabase;
import com.example.photos.R;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class PhotoDetails extends AppCompatActivity {

    private Album selectedAlbum;
    int selectedPhotoIndex;
    private List<Album> allAlbums;
    private List<Photo> allPhotos;
    private ImagePagerAdapter adapter;
    private String selectedAlbumName;

    private TextView displayTagValue;
    private EditText getTagValue;
    private Button addTag, removeTag;

    private static final int ADD_TAG_REQUEST_CODE = 456;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_details);

        // Set up the back button
        ImageView backButton = findViewById(R.id.backImageViewId);
        TextView backButtonText = findViewById(R.id.backTextViewId);
        backButton.setOnClickListener(view -> onBackPressed());
        backButtonText.setOnClickListener(view -> onBackPressed());

        // Initialize views for tags
        displayTagValue = findViewById(R.id.textview_display_tag_details);
        getTagValue = findViewById(R.id.edittext_enter_tag_value);
        addTag = findViewById(R.id.add_tag_button);
        removeTag = findViewById(R.id.remove_tag_button);

        // for Image Slide Show
        Intent intent = getIntent();
        if (intent != null) {
            selectedAlbum = (Album) intent.getSerializableExtra("ALBUM_KEY");
            selectedPhotoIndex = intent.getIntExtra("PHOTO_INDEX", -1);
            if (selectedAlbum != null && selectedPhotoIndex != -1) {
                List<Photo> albumPhotos = selectedAlbum.getPhotos();
                // Initialize the ViewPager and set the adapter
                ViewPager viewPager = findViewById(R.id.photoDetailImageViewPager);
                adapter = new ImagePagerAdapter(albumPhotos);
                viewPager.setAdapter(adapter);
                // Set the current item of the ViewPager to display the selected photo first
                viewPager.setCurrentItem(selectedPhotoIndex);
                // Update the displayed tags
                updateDisplayedTags();
            }
        }

        // for select_album_spinner
        Spinner selectAlbumSpinner = findViewById(R.id.select_album_spinner);
        allAlbums = PhotoDatabase.getAlbums();
        allPhotos = PhotoDatabase.getAllPhotos();
        List<String> albumNames = new ArrayList<>();
        for (Album album : allAlbums) {
            albumNames.add(album.getName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, albumNames);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectAlbumSpinner.setAdapter(spinnerAdapter);

        selectAlbumSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedAlbumName = albumNames.get(position);
                Button addButton = findViewById(R.id.add_button);
                Button deleteButton = findViewById(R.id.delete_button);
                Button moveButton = findViewById(R.id.move_button);
                addButton.setOnClickListener(v -> onAddButtonClicked());
                deleteButton.setOnClickListener(v -> onDeleteButtonClicked());
                moveButton.setOnClickListener(v -> onMoveButtonClicked());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Do nothing here
            }
        });
        Spinner tagSpinner = (Spinner) findViewById(R.id.tag_category_spinner);
        ArrayAdapter<CharSequence> adapterTag = ArrayAdapter.createFromResource(
                this,
                R.array.tag_category_array,
                android.R.layout.simple_spinner_item
        );
        // Tag Section
         selectedAlbum = findAlbumByName(selectedAlbumName);
        if (selectedAlbum != null && selectedPhotoIndex != -1) {
            // Get the selected photo
            List<Photo> albumPhotos = selectedAlbum.getPhotos();
            if (selectedPhotoIndex >= 0 && selectedPhotoIndex < albumPhotos.size()) {
                Photo selectedPhoto = albumPhotos.get(selectedPhotoIndex);
                // Display all tags for the selected photo in the displayTagValue TextView
                displayTagValue.setText(formatTags(selectedPhoto.getTags()));
            }
        }

       // Specify the layout to use when the list of choices appears.
        adapterTag.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner.
        tagSpinner.setAdapter(adapterTag);
        addTag.setOnClickListener(view -> {
            // Get the selected album
            selectedAlbum = findAlbumByName(selectedAlbumName);
            if (selectedAlbum != null && selectedPhotoIndex != -1) {
                // Get the selected photo
                List<Photo> albumPhotos = selectedAlbum.getPhotos();
                if (selectedPhotoIndex >= 0 && selectedPhotoIndex < albumPhotos.size()) {
                    Photo selectedPhoto = albumPhotos.get(selectedPhotoIndex);
                    // Get the selected tag category and tag value
                    String selectedTagCategory = tagSpinner.getSelectedItem().toString();
                    String tagValue = getTagValue.getText().toString();
                    // Add the tag to the selected photo
                    selectedPhoto.getTags().add(selectedTagCategory + ": " + tagValue);
                    // Update the PhotoDatabase with the changes
                    PhotoDatabase.updatePhoto(selectedAlbumName, selectedPhotoIndex, selectedPhoto);
                    // Display all tags for the selected photo in the displayTagValue TextView
                    displayTagValue.setText(formatTags(selectedPhoto.getTags()));
                    getTagValue.setText("");
                    showToast("Tag Added Successfully ");
                }
            }
        });

        // Remove Tag button
        removeTag.setOnClickListener(view -> {
            // Get the selected album
            selectedAlbum = findAlbumByName(selectedAlbumName);
            if (selectedAlbum != null && selectedPhotoIndex != -1) {
                // Get the selected photo
                List<Photo> albumPhotos = selectedAlbum.getPhotos();
                if (selectedPhotoIndex >= 0 && selectedPhotoIndex < albumPhotos.size()) {
                    Photo selectedPhoto = albumPhotos.get(selectedPhotoIndex);
                    // Get the tag value to be removed
                    String tagValueToRemove = getTagValue.getText().toString();
                    // Remove the tag from the selected photo
                    removeTagFromPhoto(selectedPhoto, tagValueToRemove);
                    // Update the PhotoDatabase with the changes
                    PhotoDatabase.updatePhoto(selectedAlbumName, selectedPhotoIndex, selectedPhoto);
                    // Display all tags for the selected photo in the displayTagValue TextView
                    displayTagValue.setText(formatTags(selectedPhoto.getTags()));
                    showToast("Tag Removed Successfully ");
                }
            }
        });
    }
    private String formatTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "No tags";
        }
        StringBuilder formattedTags = new StringBuilder();
        for (String tag : tags) {
            formattedTags.append(tag).append("\n");
        }
        return formattedTags.toString().trim();
    }

    private void onAddButtonClicked() {
        // Get the selected album
        Album selectedAlbum = findAlbumByName(selectedAlbumName);

        if (selectedAlbum != null) {
            // Create a dialog or launch a new activity to allow photo selection
            showPhotoSelectionDialog(selectedAlbum);
        }
    }
    private void onDeleteButtonClicked() {

        if (selectedAlbumName != null && selectedPhotoIndex != -1) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirm Deletion");
            builder.setMessage("Are you sure you want to delete this photo?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                // User clicked "Yes," proceed with deletion
                deletePhoto(selectedAlbumName, selectedPhotoIndex);
            });
            builder.setNegativeButton("No", (dialog, which) -> {
                // User clicked "No," do nothing
                dialog.dismiss();
            });
            // Show the alert dialog
            builder.show();
        }
    }
    // Method to delete the photo
    private void deletePhoto(String albumName, int photoIndex) {
        // Remove the photo from its selected album
        List<Album> allAlbums = PhotoDatabase.getAlbums();
        Album selectedAlbum = findAlbumByName(albumName);
        if (selectedAlbum != null) {
            List<Photo> albumPhotos = selectedAlbum.getPhotos();
            if (photoIndex >= 0 && photoIndex < albumPhotos.size()) {
                // Remove the photo from the selected album
                albumPhotos.remove(photoIndex);
                // Notify the adapter about the data change in the ViewPager
                adapter.notifyDataSetChanged();
            }
        }
    }
    private void onMoveButtonClicked() {
        // Check if the intent is not null
        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve album and photo index from the intent
            selectedAlbum = (Album) intent.getSerializableExtra("ALBUM_KEY");
            selectedPhotoIndex = intent.getIntExtra("PHOTO_INDEX", -1);
            // Check if the album and photo index are valid
            if (selectedAlbum != null && selectedPhotoIndex != -1) {
                // Retrieve album photos
                List<Photo> albumPhotos = selectedAlbum.getPhotos();
                // Initialize the ViewPager and set the adapter
                ViewPager viewPager = findViewById(R.id.photoDetailImageViewPager);
                adapter = new ImagePagerAdapter(albumPhotos);
                viewPager.setAdapter(adapter);
                // Set the current item of the ViewPager to display the selected photo first
                viewPager.setCurrentItem(selectedPhotoIndex);
                // Update the displayed tags
                updateDisplayedTags();
                // Get the selected album
                Album destinationAlbum = findAlbumByName(selectedAlbumName);
                // Check if the destination album is valid
                if (destinationAlbum != null) {
                    // Remove the photo from its original album
                    List<Photo> sourceAlbumPhotos = selectedAlbum.getPhotos();
                    // Check if the photo index is valid
                    if (selectedPhotoIndex >= 0 && selectedPhotoIndex < sourceAlbumPhotos.size()) {
                        // Get the photo that needs to be moved
                        Photo movedPhoto = sourceAlbumPhotos.remove(selectedPhotoIndex);
                        // Add the photo to the destination album
                        destinationAlbum.getPhotos().add(movedPhoto);
                        // Notify the adapter about the data change in the ViewPager
                        adapter.notifyDataSetChanged();
                        // Show Toast messages
                        showToast("Photo moved to " + destinationAlbum.getName());
                        // Delete the photo from the original album
                        deletePhoto(selectedAlbum.getName(), selectedPhotoIndex);
                        // Optionally, update the displayed tags after the move
                        updateDisplayedTags();
                    } else {
                        showToast("Invalid photo index in the source album");
                    }
                } else {
                    showToast("Invalid destination album");
                }
            } else {
                showToast("Invalid album or photo index");
            }
        } else {
            showToast("Intent is null");
        }
    }




    // Helper method to find an album by name
    private Album findAlbumByName(String albumName) {
        for (Album album : allAlbums) {
            if (album.getName().equals(albumName)) {
                return album;
            }
        }
        return null;
    }
    private void showPhotoSelectionDialog(final Album selectedAlbum) {
        // Implement a dialog or launch a new activity to display a list of photos
        // and allow the user to select one.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a Photo");
        // Create a custom adapter to display photos with image resource IDs
        AddPhotoAdapter photoAdapter = new AddPhotoAdapter(this, allPhotos);
        builder.setAdapter(photoAdapter, (dialog, which) -> {
            // User selected a photo, add it to the selected album
            Photo selectedPhoto = allPhotos.get(which);
            // Add the photo to the currently selected album
            selectedAlbum.getPhotos().add(selectedPhoto);
            // Notify the adapter about the data change in the ViewPager
            adapter.notifyDataSetChanged();
        });
        builder.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateDisplayedTags();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TAG_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Update displayed tags when returning from another activity
                updateDisplayedTags();
                showToast("Tags added successfully");
            } else {
                showToast("Adding tags canceled or failed");
            }
        }
    }
    private void updateDisplayedTags() {
        if (selectedAlbum != null && selectedPhotoIndex != -1) {
            List<Photo> albumPhotos = selectedAlbum.getPhotos();
            if (selectedPhotoIndex >= 0 && selectedPhotoIndex < albumPhotos.size()) {
                Photo selectedPhoto = albumPhotos.get(selectedPhotoIndex);
                List<String> tags = selectedPhoto != null ? selectedPhoto.getTags() : null;
                if (tags != null) {
                    displayTagValue.setText(formatTags(tags));
                    showToast("Tags updated successfully");
                } else {
                    displayTagValue.setText("No tags");
                    showToast("Tags are null");
                }
            }
        }
    }


    private void removeTagFromPhoto(Photo selectedPhoto, String tagValueToRemove) {
        List<String> photoTags = selectedPhoto.getTags();
        for (String tag : photoTags) {
            if (tag.contains(tagValueToRemove)) {
                // Remove the tag from the list
                photoTags.remove(tag);
                break; // Break the loop after removing the first occurrence
            }
        }
    }
    private void showToast(String message) {
        runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_SHORT).show());
    }
}
