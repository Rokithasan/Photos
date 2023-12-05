package com.example.photos.model;

import android.net.Uri;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

public class Photo implements Serializable {
    String filePath;

    private transient Album  associatedAlbum;
    private List<String> tags;
    private transient Uri uri;
    private int imageResourceId;

    public Photo(String filePath, List<String> tags, int imageResourceId) {
        this.filePath = filePath;
        this.tags = tags;
        this.imageResourceId = imageResourceId;
    }

    public Album getAssociatedAlbum() {
        return associatedAlbum;
    }

    public void setAssociatedAlbum(Album associatedAlbum) {
        this.associatedAlbum = associatedAlbum;
    }

    public Photo(Uri uri) {
        this.uri = uri;
    }

    // Other existing methods...

    public int getImageResourceId() {
        return imageResourceId;
    }

    public void setImageResourceId(int imageResourceId) {
        this.imageResourceId = imageResourceId;
    }

    public String getFilePath() {
        return filePath;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public boolean matchesSearch(String tagType, String tagValue) {
        for (String tag : tags) {
            // Check if the tag matches the search criteria
            if (tag.toLowerCase().startsWith(tagType.toLowerCase() + ":") &&
                    tag.toLowerCase().contains(tagValue.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        // Serialize Uri as a String
        out.writeObject(uri != null ? uri.toString() : null);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        // Deserialize String back to Uri
        String uriString = (String) in.readObject();
        uri = (uriString != null) ? Uri.parse(uriString) : null;
    }

    public Uri getUri() {
        return uri;
    }

}
