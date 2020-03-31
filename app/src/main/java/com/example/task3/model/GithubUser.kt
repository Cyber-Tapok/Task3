package com.example.task3.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class GithubUser(
    val login: String,
    val avatar_url: String
) : Parcelable