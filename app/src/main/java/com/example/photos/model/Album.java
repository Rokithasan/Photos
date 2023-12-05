package com.example.photos.model;


import android.net.Uri;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Album implements Serializable {
    private String name;  // Name is used as the identifier
    private List<Photo> photos;

    public Album(String name, List<Photo> photos) {
        this.name = name;
        this.photos = (photos != null) ? photos : new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(name, album.name) &&
                Objects.equals(photos, album.photos);
    }

    public List<Photo> searchPhotos(String tagType, String tagValue) {
        List<Photo> matchingPhotos = new ArrayList<>();
        for (Photo photo : photos) {
            if (photo.matchesSearch(tagType, tagValue)) {
                matchingPhotos.add(photo);
            }
        }
        return matchingPhotos;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, photos);
    }

    public void addPhoto(Photo photo) {
        photos.add(photo);
    }

    // Add a method to get URIs from photos
    public List<Uri> getPhotoUris() {
        List<Uri> uris = new ArrayList<>();
        for (Photo photo : photos) {
            // Assuming you have a getUri method in your Photo class
            uris.add(photo.getUri());
        }
        return uris;
    }
}
