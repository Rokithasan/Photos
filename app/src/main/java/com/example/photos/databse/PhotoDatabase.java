package com.example.photos.databse;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.photos.R;
import com.example.photos.model.Album;
import com.example.photos.model.Photo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PhotoDatabase implements Serializable {

    private static final String TAG = "PhotoDatabase";
    private static final String PREF_NAME = "MyPhotoAppPrefs";
    private static final String ALBUMS_KEY = "albums";
    private static final String PHOTOS_KEY = "photos";

    private static PhotoDatabase instance;

    public static List<Album> albums = new ArrayList<>();
    private static List<Photo> allPhotos = new ArrayList<>();

    public static PhotoDatabase getInstance() {
        if (instance == null) {
            instance = new PhotoDatabase();
        }
        return instance;
    }

    // ...

    // Initialize our photo database with some sample data
    static {
        // Add photos from the drawable folder
        Photo photo1 = new Photo("acura.jpg", new ArrayList<>(), R.drawable.acura);
        Photo photo2 = new Photo("bugatti_mistral.jpg", new ArrayList<>(), R.drawable.bugatti_mistral);
        Photo photo3 = new Photo("jaguar.jpg", new ArrayList<>(), R.drawable.jaguar);
        Photo photo4 = new Photo("lamborghini.jpg", new ArrayList<>(), R.drawable.lamborghini);
        Photo photo5 = new Photo("lamborghini_huracan.jpg", new ArrayList<>(), R.drawable.lamborghini_huracan);
        Photo photo6 = new Photo("porsche.jpg", new ArrayList<>(), R.drawable.porsche);
        allPhotos.add(photo1);
        allPhotos.add(photo2);
        allPhotos.add(photo3);
        allPhotos.add(photo4);
        allPhotos.add(photo5);
        allPhotos.add(photo6);
        // Create an album with these photos
        Album albumWithDrawablePhotos = new Album("Stock", allPhotos);
        albums.add(albumWithDrawablePhotos);
        // Add more albums as needed
    }

    public static List<Album> getAlbums() {
        Log.d(TAG, "getAlbums: Albums retrieved");
        return albums;
    }

    public static List<Photo> getAllPhotos() {
        Log.d(TAG, "getAllPhotos: All photos retrieved");
        return allPhotos;
    }

    public static void addAlbum(Album album) {
        albums.add(album);
        Log.d(TAG, "addAlbum: Album added - " + album.getName());
    }

    public static void removeAlbum(Album album) {
        albums.remove(album);
        Log.d(TAG, "removeAlbum: Album removed - " + album.getName());
    }

    public static void updatePhoto(String albumName, int photoIndex, Photo updatedPhoto) {
        // ...

        Log.d(TAG, "updatePhoto: Photo updated in album - " + albumName);
    }

    public static Album findAlbumByName(String albumName) {
        // ...

        Log.d(TAG, "findAlbumByName: Album found - " + albumName);
        return null;
    }

    public static void addPhoto(Photo photo, Album album) {
        // ...

        Log.d(TAG, "addPhoto: Photo added to album - " + album.getName());
    }

    public static void saveData(Context context) {
        // ...

        Log.d(TAG, "saveData: Data saved");
    }

    public static List<Photo> getPhotosForAlbum(Album album) {
        List<Photo> albumPhotos = new ArrayList<>();
        if (album != null) {
            for (Photo photo : allPhotos) {
                if (photo.getAssociatedAlbum() != null && photo.getAssociatedAlbum().equals(album)) {
                    albumPhotos.add(photo);
                }
            }
        }
        return albumPhotos;
    }

    // ...
}
