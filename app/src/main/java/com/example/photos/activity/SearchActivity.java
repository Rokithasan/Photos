package com.example.photos.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.photos.R;
import com.example.photos.adapter.SearchResultsAdapter;
import com.example.photos.model.Photo;

import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SearchResultsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        recyclerView = findViewById(R.id.searchPhotoRecycler);
        adapter = new SearchResultsAdapter();

        // Get the search results from the Intent
        List<Photo> searchResults = (List<Photo>) getIntent().getSerializableExtra("searchResults");

        // Set the search results to the adapter
        adapter.setSearchResults(searchResults);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}