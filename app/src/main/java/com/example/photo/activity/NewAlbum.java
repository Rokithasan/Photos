package com.example.photo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.photo.MainActivity;
import com.example.photo.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewAlbum extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_album);

        final EditText albumName =  (EditText) findViewById(R.id.albumName);
        Button createButton =  (Button) findViewById(R.id.create),
                backButton = (Button) findViewById(R.id.cancel);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!albumName.getText().toString().isEmpty() && !MainActivity.albums.contains(albumName.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
                    try {
                        FileOutputStream fileOutputStream = openFileOutput(albumName.getText().toString()+".list", MODE_PRIVATE);
                        MainActivity.albums.add(albumName.getText().toString());
                        write();
                        finish();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {

                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void back() {

        finish();
    }

    public void write(){
        try {
            String str = "";
            if (MainActivity.albums.size() > 0) {
                str = MainActivity.albums.get(0);
            }

            FileOutputStream fileOutputStream = openFileOutput("albums.albm", MODE_PRIVATE);
            for (int i = 1; i < MainActivity.albums.size(); i++) {
                str = str.concat("\n" + MainActivity.albums.get(i));
            }

            fileOutputStream.write(str.getBytes());

        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
