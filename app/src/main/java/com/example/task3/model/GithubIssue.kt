package com.example.task3.model

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "issues")
data class GithubIssue(
    val title: String,
    @PrimaryKey val number: Int,
    val state: String,
    @Embedded(prefix = "user") val user: GithubUser,
    val body: String?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String?,
    @SerializedName("closed_at") val closedAt: String?
) : Parcelable