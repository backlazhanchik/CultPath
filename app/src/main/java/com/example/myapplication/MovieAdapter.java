package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private final OnMovieClickListener onMovieClick;

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    public MovieAdapter(List<Movie> movies, OnMovieClickListener onMovieClick) {
        this.movies = movies;
        this.onMovieClick = onMovieClick;
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final TextView tvDescription;
        final TextView tvCategory;
        final TextView tvLocation;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_movie_title);
            tvDescription = itemView.findViewById(R.id.tv_movie_description);
            tvCategory = itemView.findViewById(R.id.tv_movie_category);
            tvLocation = itemView.findViewById(R.id.tv_movie_location);
        }
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        holder.tvTitle.setText(movie.getTitle());
        holder.tvDescription.setText(movie.getDescription());
        holder.tvCategory.setText(movie.getCategory());
        holder.tvLocation.setText(movie.getLocation());

        holder.itemView.setOnClickListener(v -> {
            onMovieClick.onMovieClick(movie);
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void updateMovies(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }
}