package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.database.DatabaseHelper;

public class CourseDetailFragment extends Fragment {

    private TextView tvCourseTitle;
    private TextView tvCourseDescription;
    private WebView webView;
    private Button btnAddToWalkthrough;
    private Button btnAddToFavorites;
    private Button btnBack;
    private DatabaseHelper databaseHelper;
    private long userId = -1;
    private Course course;

    private static final String ARG_COURSE = "course";

    public static CourseDetailFragment newInstance(Course course) {
        CourseDetailFragment fragment = new CourseDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_COURSE, course);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        tvCourseTitle = view.findViewById(R.id.tv_course_title);
        tvCourseDescription = view.findViewById(R.id.tv_course_description);
        webView = view.findViewById(R.id.webView);
        btnAddToWalkthrough = view.findViewById(R.id.btn_add_to_walkthrough);
        btnAddToFavorites = view.findViewById(R.id.btn_add_to_favorites);
        btnBack = view.findViewById(R.id.btn_back);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(requireContext());

        // Get user ID from shared preferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("CulturalAppPrefs", 0);
        userId = sharedPref.getLong("USER_ID", -1);

        // Get course from arguments
        Bundle args = getArguments();
        if (args != null) {
            course = args.getParcelable(ARG_COURSE);
        } else {
            return;
        }

        // Set course details
        tvCourseTitle.setText(course.getTitle());
        tvCourseDescription.setText(course.getDescription());

        // Configure WebView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });

        // Load course link if available
        if (!course.getLink().isEmpty()) {
            webView.loadUrl(course.getLink());
        }

        // Set click listeners
        btnAddToWalkthrough.setOnClickListener(v -> {
            if (userId != -1L) {
                databaseHelper.addCourseToWalkthrough(userId, course);
                Toast.makeText(requireContext(), "Добавлено в пошаговое руководство",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(),
                        "Пожалуйста, войдите в систему, чтобы сохранить курсы",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnAddToFavorites.setOnClickListener(v -> {
            if (userId != -1L) {
                databaseHelper.addCourseToFavorites(userId, course);
                Toast.makeText(requireContext(), "Добавлено в избранное",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(),
                        "Пожалуйста, войдите в систему, чтобы сохранить курсы",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }
}