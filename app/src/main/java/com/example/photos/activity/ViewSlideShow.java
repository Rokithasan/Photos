package com.example.photos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.photos.R;
import com.example.photos.adapter.ImagePagerAdapter;
import com.example.photos.adapter.PhotoAdapter;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;

import java.util.List;

public class ViewSlideShow extends AppCompatActivity {

    private Album selectedAlbum;
    ImagePagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_slide_show);

        // Set up the back button
        ImageView backButton = findViewById(R.id.backImageViewId);
        TextView backButtonText = findViewById(R.id.backTextViewId);

        backButton.setOnClickListener(view -> onBackPressed());
        backButtonText.setOnClickListener(view -> onBackPressed());

        Intent intent = getIntent();
        if (intent != null) {
            selectedAlbum = (Album) intent.getSerializableExtra("ALBUM_KEY");
            // Check if selectedAlbum is not null and has photos
            if (selectedAlbum != null && selectedAlbum.getPhotos() != null && !selectedAlbum.getPhotos().isEmpty()) {
                List<Photo> albumPhotos = selectedAlbum.getPhotos();
                // Pass the photos to the ImagePagerAdapter
                adapter = new ImagePagerAdapter(albumPhotos);
                // Set up the ViewPager and ImagePagerAdapter
                ViewPager viewPager = findViewById(R.id.photoDetailImageViewPager);
                viewPager.setAdapter(adapter);
            }
        }
    }
}