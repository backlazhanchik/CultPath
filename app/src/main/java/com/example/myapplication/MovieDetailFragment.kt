package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.culturalapp.database.DatabaseHelper

class MovieDetailFragment : Fragment() {

    private lateinit var tvMovieTitle: TextView
    private lateinit var tvMovieDescription: TextView
    private lateinit var webView: WebView
    private lateinit var btnAddToWalkthrough: Button
    private lateinit var btnAddToFavorites: Button
    private lateinit var btnBack: Button
    private lateinit var databaseHelper: DatabaseHelper
    private var userId: Long = -1
    private lateinit var movie: Movie

    companion object {
        private const val ARG_MOVIE = "movie"

        fun newInstance(movie: Movie): MovieDetailFragment {
            val fragment = MovieDetailFragment()
            val args = Bundle()
            args.putParcelable(ARG_MOVIE, movie)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tvMovieTitle = view.findViewById(R.id.tv_movie_title)
        tvMovieDescription = view.findViewById(R.id.tv_movie_description)
        webView = view.findViewById(R.id.webView)
        btnAddToWalkthrough = view.findViewById(R.id.btn_add_to_walkthrough)
        btnAddToFavorites = view.findViewById(R.id.btn_add_to_favorites)
        btnBack = view.findViewById(R.id.btn_back)


        databaseHelper = DatabaseHelper(requireContext())


        val sharedPref = requireActivity().getSharedPreferences("CulturalAppPrefs", 0)
        userId = sharedPref.getLong("USER_ID", -1)


        movie = arguments?.getParcelable(ARG_MOVIE) ?: return


        tvMovieTitle.text = movie.title
        tvMovieDescription.text = movie.description


        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                return false
            }
        }


        if (movie.link.isNotEmpty()) {
            webView.loadUrl(movie.link)
        }


        btnAddToWalkthrough.setOnClickListener {
            if (userId != -1L) {
                databaseHelper.addMovieToWalkthrough(userId, movie)
                Toast.makeText(requireContext(), "Добавлено в пошаговое руководство", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Пожалуйста, войдите в систему, чтобы сохранить фильмы", Toast.LENGTH_SHORT).show()
            }
        }


        btnAddToFavorites.setOnClickListener {
            if (userId != -1L) {
                databaseHelper.addMovieToFavorites(userId, movie)
                Toast.makeText(requireContext(), "Добавлено в избранное", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Пожалуйста, войдите в систему, чтобы сохранить фильмы", Toast.LENGTH_SHORT).show()
            }
        }


        btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}