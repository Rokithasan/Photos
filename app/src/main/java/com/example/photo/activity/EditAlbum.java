package com.example.photo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.photo.MainActivity;
import com.example.photo.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @author David Duong dd831
 * @author Aditya Jani amj165
 */

public class EditAlbum extends AppCompatActivity {

    /**
     * Sets Data
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);

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
                int index = MainActivity.getIndex();
                if (!albumName.getText().toString().isEmpty() && !MainActivity.albums.contains(albumName.getText().toString())) {

                    Toast.makeText(getApplicationContext(), MainActivity.albums.get(index), Toast.LENGTH_SHORT).show();
                    try {

                        File old = new File((getFilesDir() + File.separator + MainActivity.albums.get(index)+".list"));
                        FileOutputStream fileOutputStream = openFileOutput(albumName.getText().toString()+".list", MODE_PRIVATE);

                        String path = old.toPath().toUri().toString();
                        FileReader fileInputStream = new FileReader(old);

                        String str = "";
                        int check;
                        while ((check = fileInputStream.read()) != -1) {
                            str = str + ((char) check + "");
                        }

                        fileOutputStream.write(str.getBytes());

                        getApplicationContext().deleteFile(new File(MainActivity.albums.get(index)+".list").getName());
//                    .delete();
                        MainActivity.albums.remove(index);
                        MainActivity.albums.add(index,albumName.getText().toString());
                        index = -1;
                        write();

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
                                android.R.layout.simple_list_item_1, MainActivity.albums);

                        MainActivity.gridView.setAdapter(arrayAdapter);
                        finish();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Returns to HomeScreen
     */
    private void back() {
        finish();
    }

    /**
     * Saves app data
     */
    public void write(){
// FILE PATH    /data/user/0/com.AJ_David.photos/files/albums.albm
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

        }catch(FileNotFoundException e){
            e.printStackTrace();
        } catch(ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Reads and stores list of Albums
     */
    public String[] read() {
        String[] strings = {};

        try {
            FileInputStream fileInputStream = openFileInput("albums.albm");

            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            ArrayList<String> list = new ArrayList<String>();
            String lineIn;

            while ((lineIn = bufferedReader.readLine()) != null) {
                list.add(lineIn);
            }

            strings = new String[list.size()];

            for (int i = 0; i < list.size(); i++){
                strings[i] = list.get(i);
            }

            return strings;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

}
