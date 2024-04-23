package com.example.github_api.API.SERVICE

import com.example.github_api.API.MODEL.DetailUserGithub
import com.example.github_api.API.MODEL.GithubUserResponse
import com.example.github_api.API.MODEL.Item
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface GithubInterface {
    // GET request to search for users
    @JvmSuppressWildcards
    @GET("users")
    suspend fun getUser(): MutableList<Item>@JvmSuppressWildcards

    @GET("users/{username}")
    suspend fun getDetailUser(@Path("username") username :String): DetailUserGithub

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username :String): MutableList<Item>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username :String): MutableList<Item>

    @JvmSuppressWildcards
    @GET("search/users")
    suspend fun searchUser(@QueryMap params: Map<String, Any>): GithubUserResponse
}