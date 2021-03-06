package com.example.task3.retrofit

import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://api.github.com"
const val USERNAME = "Cyber-Tapok"
const val PASSWORD = "c*"
const val REPOS = "TEST"

class RetrofitClient {
    private val okHttpClient: OkHttpClient = OkHttpClient().newBuilder().addInterceptor { chain ->
        val originalRequest: Request = chain.request()
        val builder: Request.Builder = originalRequest.newBuilder().header(
            "Authorization",
            Credentials.basic(USERNAME, PASSWORD)
        )
        val newRequest: Request = builder.build()
        chain.proceed(newRequest)
    }.build()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getService(): GitHubService {
        return retrofit.create(GitHubService::class.java)
    }
}
