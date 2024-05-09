package com.example.github_api.Detailuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.github_api.API.CLIENT.ApiClient
import com.example.github_api.API.MODEL.Item
import com.example.github_api.ResultVM.Results
import com.example.github_api.local.DbModule
import kotlinx.coroutines.launch

/**
 * ViewModel for the DetailUser feature.
 * This ViewModel is responsible for managing the data for the DetailUser feature.
 *
 * @param db The database module used to interact with the database.
 */
class DetailViewModel(private val db: DbModule) : ViewModel() {

    /**
     * LiveData objects that represent the results of various operations.
     */
    val resultDetailUser = MutableLiveData<Results>()
    val resultFollowers = MutableLiveData<Results>()
    val resultFollowing = MutableLiveData<Results>()

    /**
     * Boolean that represents whether a user is a favorite.
     */
    private var isFavorite = false

    /**
     * LiveData objects that represent the results of favorite operations.
     */
    val resultSuccess = MutableLiveData<Boolean>()
    val resultFailFav = MutableLiveData<Boolean>()

    /**
     * Function to set a user as a favorite or remove them from favorites.
     *
     * @param item The user to add or remove from favorites.
     * ?. is because item can be null if the user is not found in the database.
     */
    fun setFav(item: Item?) {
        viewModelScope.launch {
            item?.let {
                if (isFavorite) {
                    db.userDao.delete(item)
                    resultFailFav.value = true
                } else {
                    db.userDao.insert(item)
                    resultSuccess.value = true
                }
            }
            isFavorite = !isFavorite
        }
    }

    /**
     * Function to find a favorite user.
     *
     * @param id The id of the user to find. So
     * @param listenFavorite The function to call if the user is found. So it can be used to update the UI.
     */
    fun findFavorite(id: Int, listenFavorite: () -> Unit) {
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            if (user != null) {
                listenFavorite()
                isFavorite = true
            }
        }
    }

    /**
     * Functions to get user details, followers, and following.
     *
     * @param username The username of the user to get details for.
     */
    fun getDetailUser(username: String) {
        fetchData(username, resultDetailUser) { ApiClient.githubservice.getDetailUser(it) }
    }

    fun getFollowers(username: String) {
        fetchData(username, resultFollowers) { ApiClient.githubservice.getFollowers(it) }
    }

    fun getFollowing(username: String) {
        fetchData(username, resultFollowing) { ApiClient.githubservice.getFollowing(it) }
    }

    /**
     * Private function to fetch data from the API.
     *
     * @param username The username of the user to fetch data for.
     * @param liveData The LiveData object to post the results to.
     * @param apiCall The API call to make.
     */
    private fun fetchData(
            username: String,
            liveData: MutableLiveData<Results>,
            apiCall: suspend (String) -> Any
    ) {
        viewModelScope.launch {
            try {
                liveData.value = Results.Loading(true)
                val response = apiCall(username)
                liveData.value = Results.Success(response)
            } catch (e: Exception) {
                liveData.value = Results.Error(e)
            } finally {
                liveData.value = Results.Loading(false)
            }
        }
    }

    /**
     * Factory for creating instances of DetailViewModel with a specific DbModule.
     *
     * @param db The DbModule to use when creating DetailViewModel instances.
     */
    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {

        /**
         * Creates a new instance of a ViewModel of the given Class.
         *
         * @param modelClass The class of the ViewModel to create an instance of.
         */
        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T

    }

}