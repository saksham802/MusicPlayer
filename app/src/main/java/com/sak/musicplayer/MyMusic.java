package com.sak.musicplayer;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MyMusic extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 1; // Renamed for clarity
    private ArrayList<String> musicList;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_music);

        listView = findViewById(R.id.list_item);
        musicList = new ArrayList<>();

        // Check if permission is already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            loadMusic(); // Permission already granted, fetch music
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_REQUEST_CODE);
        }
    }

    // This method loads the list of music files and displays them
    private void loadMusic() {
        getMusic();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, musicList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            // You can implement what happens when an item is clicked here
            String selectedSong = musicList.get(i);
            Toast.makeText(this, "Playing: " + selectedSong, Toast.LENGTH_SHORT).show();
        });
    }

    // This method queries the device's external storage for audio files and adds them to the list
    private void getMusic() {
        ContentResolver contentResolver = getContentResolver();
        Uri songUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        try (Cursor cursor = contentResolver.query(songUri, null, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int songTitleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                int songArtistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                int songLocationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

                do {
                    String currentTitle = cursor.getString(songTitleIndex);
                    String currentArtist = cursor.getString(songArtistIndex);
                    String currentLocation = cursor.getString(songLocationIndex);

                    musicList.add(currentTitle + "\n" + currentArtist + "\n" + currentLocation);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Failed to load music", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                loadMusic(); // Permission granted, load the music
            } else {
                Toast.makeText(this, "Permission denied. Please enable permission to access music files.", Toast.LENGTH_LONG).show();
                finish(); // Close the activity if permission is not granted
            }
        }
    }
}
