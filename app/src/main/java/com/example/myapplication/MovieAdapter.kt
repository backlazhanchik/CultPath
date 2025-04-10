package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MovieAdapter(
    private var movies: List<Movie>,
    private val onMovieClick: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_movie_title)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_movie_description)
        val tvCategory: TextView = itemView.findViewById(R.id.tv_movie_category)
        val tvLocation: TextView = itemView.findViewById(R.id.tv_movie_location)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        
        holder.tvTitle.text = movie.title
        holder.tvDescription.text = movie.description
        holder.tvCategory.text = movie.category
        holder.tvLocation.text = movie.location
        
        holder.itemView.setOnClickListener {
            onMovieClick(movie)
        }
    }

    override fun getItemCount(): Int = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }
}
