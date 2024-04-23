package com.example.github_api.API.CLIENT

import android.util.Log
import com.example.github_api.API.SERVICE.GithubInterface
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient
{
    private val okHttpClient = OkHttpClient.Builder()
        // add token
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        // add token
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "token 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")
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.github.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val githubservice = retrofit.create<GithubInterface>(GithubInterface::class.java)
}
