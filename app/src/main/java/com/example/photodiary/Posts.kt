package com.example.photodiary

import android.net.Uri

data class Posts(
    val storyTitle: String,
    val storyLocation: String,
    val storyDescription: String,
    val storyImage: Uri?,
    val storyRating: Float
)
