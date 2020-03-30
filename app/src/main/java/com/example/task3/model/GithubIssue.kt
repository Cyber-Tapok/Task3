package com.example.task3.model


class GithubIssue(
    val title: String,
    val number: Int,
    val state: String,
    val user: GithubUser,
    val body: String,
    val created_at: String,
    val updated_at: String,
    val closed_at: String

)