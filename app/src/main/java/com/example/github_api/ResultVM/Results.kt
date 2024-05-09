package com.example.github_api.ResultVM

/**
 * Sealed class representing the different states of a result.
 * This class is used to handle the different states of a network request.
 */
sealed class Results {

    /**
     * Represents the success state of a result.
     *
     * @property data The data returned from the network request.
     */
    data class Success<out T>(val data: T) : Results()

    /**
     * Represents the error state of a result.
     *
     * @property exception The exception thrown during the network request.
     */
    data class Error(val exception: Throwable) : Results()

    /**
     * Represents the loading state of a result.
     *
     * @property isLoading A boolean indicating whether a network request is in progress or not
     */
    data class Loading(val isLoading: Boolean) : Results()
}