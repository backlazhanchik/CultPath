package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.tabs.TabLayout;
import com.example.myapplication.database.DatabaseHelper;

import java.util.List;
import java.util.Objects;

public class ProfileFragment extends Fragment {

    private TextView tvName;
    private TextView tvEmail;
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private CourseAdapter courseAdapter;
    private DatabaseHelper databaseHelper;
    private long userId = -1;
    private String userEmail = "";
    private String userName = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tabLayout = view.findViewById(R.id.tab_layout);
        recyclerView = view.findViewById(R.id.recycler_view);

        databaseHelper = new DatabaseHelper(requireContext());

        android.content.SharedPreferences sharedPref = requireActivity()
                .getSharedPreferences("CulturalAppPrefs", 0);
        userId = sharedPref.getLong("USER_ID", -1);
        userEmail = sharedPref.getString("USER_EMAIL", "");

        if (userId != -1) {
            User user = databaseHelper.getUserById(userId);
            if (user != null) {
                userName = user.getFirstName() + " " + user.getLastName();
            }
        }

        tvName.setText(userName);
        tvEmail.setText(userEmail);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        courseAdapter = new CourseAdapter(List.of(), course -> {
            CourseDetailFragment fragment = CourseDetailFragment.newInstance(course);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });
        recyclerView.setAdapter(courseAdapter);

        setupTabLayout();
    }

    private void setupTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText("Прохождение"));
        tabLayout.addTab(tabLayout.newTab().setText("Избранное"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateCourseList(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        updateCourseList(0);
    }

    private void updateCourseList(int tabPosition) {
        if (userId == -1) return;

        List<Course> courses;
        if (tabPosition == 0) {
            courses = databaseHelper.getWalkthroughCourses(userId);
        } else {
            courses = databaseHelper.getFavoriteCourses(userId);
        }

        courseAdapter.updateCourses(courses);
    }
}