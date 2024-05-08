package com.example.github_api.API.CLIENT

import android.util.Log
import com.example.github_api.API.SERVICE.GithubInterface
import com.example.github_api.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient
{
    val mySuperScretKey = BuildConfig.KEY
    val BASE_URL = BuildConfig.BASE_URL
    private val okHttpClient = OkHttpClient.Builder()
        // add token
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        // add token
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", mySuperScretKey)
                .build()
            chain.proceed(request)
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
//    add serivce
    val githubservice = retrofit.create<GithubInterface>(GithubInterface::class.java)
}
