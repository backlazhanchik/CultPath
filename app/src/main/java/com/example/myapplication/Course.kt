package com.example.myapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val location: String,
    val category: String,
    val ageGroup: String,
    val link: String,
    var isFavorite: Boolean = false,
    var isInWalkthrough: Boolean = false
) : Parcelable
