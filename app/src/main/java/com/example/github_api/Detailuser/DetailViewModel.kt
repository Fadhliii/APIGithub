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

class DetailViewModel(private val db: DbModule) : ViewModel() {
    val resultDetailUser = MutableLiveData<Results>()
    val resultFollowers = MutableLiveData<Results>()
    val resultFollowing = MutableLiveData<Results>()
    private var isFavorite = false
    val resultSuccess = MutableLiveData<Boolean>()
    val resultFailFav = MutableLiveData<Boolean>()

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

    fun findFavorite(id: Int, listenFavorite: () -> Unit) {
        viewModelScope.launch {
            val user = db.userDao.findById(id)
            if (user != null) {
                listenFavorite()
                isFavorite = true
            }
        }
    }

//    !!getDetailUser, getFollowers, getFollowing
    fun getDetailUser(username: String) {
        fetchData(username, resultDetailUser) { ApiClient.githubservice.getDetailUser(it) }
    }

    fun getFollowers(username: String) {
        fetchData(username, resultFollowers) { ApiClient.githubservice.getFollowers(it) }
    }

    fun getFollowing(username: String) {
        fetchData(username, resultFollowing) { ApiClient.githubservice.getFollowing(it) }
    }


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

    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T = DetailViewModel(db) as T

    }

}