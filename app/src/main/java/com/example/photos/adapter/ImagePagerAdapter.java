package com.example.photos.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.photos.model.Photo;

import java.util.List;

public class ImagePagerAdapter extends PagerAdapter {

    private List<Photo> photos;

    public ImagePagerAdapter(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        // Load the image into the ImageView using the Uri if available; otherwise, use the resource ID
        Photo photo = photos.get(position);
        Uri photoUri = photo.getUri();
        if (photoUri != null) {
            // Load image using your preferred image loading library (e.g., Picasso, Glide)
            loadImageIntoImageView(imageView, photoUri);
        } else {
            // Load image from resource ID
            imageView.setImageResource(photo.getImageResourceId());
        }
        container.addView(imageView);
        return imageView;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }

    // Implement this method to load the image into the ImageView using the Uri
    private void loadImageIntoImageView(ImageView imageView, Uri photoUri) {
        Glide.with(imageView.getContext()).load(photoUri).into(imageView);
    }

    // Update the photos and notify the adapter about the change
    public void updatePhotos(List<Photo> newPhotos) {
        this.photos = newPhotos;
        notifyDataSetChanged();
    }
}
