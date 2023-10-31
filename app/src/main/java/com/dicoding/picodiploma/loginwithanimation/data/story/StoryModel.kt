package com.dicoding.picodiploma.loginwithanimation.data.story

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
@Parcelize
data class StoryModel(
    val id: String,
    val userName: String,
    val avtUrl: String,
    val desc: String,
) : Parcelable
