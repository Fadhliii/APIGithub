package com.example.github_api.API.MODEL
import com.example.github_api.API.MODEL.Item

/**
 * Data class representing the response from the Github API.
 *
 * @property incomplete_results Boolean indicating whether the results are incomplete.
 * @property items List of Item objects representing the users returned by the search.
 * @property useritems List of DetailUserGithub objects representing detailed information about the users.
 * @property total_count Integer representing the total count of the results.
 */
data class GithubUserResponse(
		val incomplete_results: Boolean,
		val items: MutableList<Item>,
		val useritems: MutableList<DetailUserGithub>,
		val total_count: Int
)