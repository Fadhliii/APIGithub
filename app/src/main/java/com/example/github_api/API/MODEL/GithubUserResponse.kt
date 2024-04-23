package com.example.github_api.API.MODEL
import com.example.github_api.API.MODEL.Item

data class GithubUserResponse(
    val incomplete_results: Boolean,
    val items: MutableList<Item>,
    val useritems: MutableList<DetailUserGithub>,
    val total_count: Int
)