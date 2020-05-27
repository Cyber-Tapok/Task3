package com.example.task3.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GithubIssue(
    val title: String,
    val number: Int,
    val state: String,
    val user: GithubUser,
    val body: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("closed_at") val closedAt: String
) : Parcelable