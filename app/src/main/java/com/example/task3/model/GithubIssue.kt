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
    @SerializedName("created_at") var createdAt: String,
    @SerializedName("updated_at") var updatedAt: String?,
    @SerializedName("closed_at") var closedAt: String?
) : Parcelable