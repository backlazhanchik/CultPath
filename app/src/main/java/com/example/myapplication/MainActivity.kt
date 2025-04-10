package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        

        if (savedInstanceState == null) {
            loadFragment(CoursesFragment())
        }
        

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_courses -> {
                    loadFragment(CoursesFragment())
                    true
                }
                R.id.nav_movies -> {
                    loadFragment(MoviesFragment())
                    true
                }
                R.id.nav_chatbot -> {
                    loadFragment(ProfileFragment())
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ChatbotActivity())
                    true
                }
                else -> false
            }
        }
    }
    
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
