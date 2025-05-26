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

public class MovieDetailFragment extends Fragment {

    private TextView tvMovieTitle;
    private TextView tvMovieDescription;
    private WebView webView;
    private Button btnAddToWalkthrough;
    private Button btnAddToFavorites;
    private Button btnBack;
    private DatabaseHelper databaseHelper;
    private long userId = -1;
    private Movie movie;

    private static final String ARG_MOVIE = "movie";

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        tvMovieTitle = view.findViewById(R.id.tv_movie_title);
        tvMovieDescription = view.findViewById(R.id.tv_movie_description);
        webView = view.findViewById(R.id.webView);
        btnAddToWalkthrough = view.findViewById(R.id.btn_add_to_walkthrough);
        btnAddToFavorites = view.findViewById(R.id.btn_add_to_favorites);
        btnBack = view.findViewById(R.id.btn_back);

        // Initialize database helper
        databaseHelper = new DatabaseHelper(requireContext());

        // Get user ID from shared preferences
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("CulturalAppPrefs", 0);
        userId = sharedPref.getLong("USER_ID", -1);

        // Get movie from arguments
        Bundle args = getArguments();
        if (args != null) {
            movie = args.getParcelable(ARG_MOVIE);
        } else {
            return;
        }

        // Set movie details
        tvMovieTitle.setText(movie.getTitle());
        tvMovieDescription.setText(movie.getDescription());

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

        // Load movie link if available
        if (!movie.getLink().isEmpty()) {
            webView.loadUrl(movie.getLink());
        }

        // Set click listeners
        btnAddToWalkthrough.setOnClickListener(v -> {
            if (userId != -1L) {
                databaseHelper.addMovieToWalkthrough(userId, movie);
                Toast.makeText(requireContext(), "Добавлено в пошаговое руководство",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(),
                        "Пожалуйста, войдите в систему, чтобы сохранить фильмы",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnAddToFavorites.setOnClickListener(v -> {
            if (userId != -1L) {
                databaseHelper.addMovieToFavorites(userId, movie);
                Toast.makeText(requireContext(), "Добавлено в избранное",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(),
                        "Пожалуйста, войдите в систему, чтобы сохранить фильмы",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnBack.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager().popBackStack();
        });
    }
}