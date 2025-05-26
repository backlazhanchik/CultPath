package com.example.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            loadFragment(new CoursesFragment());
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_courses) {
                loadFragment(new CoursesFragment());
                return true;
            } else if (itemId == R.id.nav_movies) {
                loadFragment(new MoviesFragment());
                return true;
            } else if (itemId == R.id.nav_chatbot) {
                loadFragment(new ProfileFragment());
                return true;
            } else if (itemId == R.id.nav_profile) {
                loadFragment(new ChatbotActivity());
                return true;
            } else {
                return false;
            }
        });
    }
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}