package com.example.photos.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.photos.R;
import com.example.photos.model.Photo;

import java.util.List;

public class AddPhotoAdapter extends ArrayAdapter<Photo> {


    public AddPhotoAdapter(@NonNull Context context, @NonNull List<Photo> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.for_add_photos, parent, false);
        }
        ImageView photoImageView = convertView.findViewById(R.id.photoImageView_for_add);
        Photo photo = getItem(position);
        if (photo != null) {
            photoImageView.setImageResource(photo.getImageResourceId());
        }

        return convertView;
    }
}
