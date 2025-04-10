package com.example.myapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val description: String,
    val location: String,
    val category: String,
    val ageGroup: String,
    val link: String,
    val isPopular: Boolean = false,
    var isFavorite: Boolean = false,
    var isInWalkthrough: Boolean = false
) : Parcelable
