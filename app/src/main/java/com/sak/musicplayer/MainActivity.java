package com.sak.musicplayer;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    ConstraintLayout constraintLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Initialize fragments
        Fragment homeFragment = new HomeFrag();
        Fragment profileFragment = new ProfileFrag();
        Fragment shorts = new ShortPlayer();

        // Set the default fragment
        setCurrentFragment(homeFragment);

        // Set up the bottom navigation
        BottomNavigationView bottomNavigation = findViewById(R.id.bottomnav);
        bottomNavigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.home) {
                    setCurrentFragment(homeFragment);
                    return true;
                } else if (itemId == R.id.search) {
                    setCurrentFragment(shorts);
                    return true;
                } else if (itemId == R.id.profile) {
                    setCurrentFragment(profileFragment);
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    // Method to set the current fragment
    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, fragment)
                .commit();
    }
}
