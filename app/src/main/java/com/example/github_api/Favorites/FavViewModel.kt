package com.example.github_api.Favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github_api.local.DbModule
/**
 * ViewModel for the Favorites feature.
 * This ViewModel is responsible for managing the data for the Favorites feature.
 *
 * @property dbModule The database module used to interact with the database.
 */
class FavViewModel(private val dbModule: DbModule) :ViewModel() {

    // Public LiveData object that can be observed for changes
    val resultFailFav: LiveData<Boolean> = MutableLiveData()

    // Private MutableLiveData object that can be modified within this class
    val _resultFailFav = MutableLiveData<Boolean>(this.resultFailFav.value)


    // getuserFavorite function to get all the favorite users from the database
    fun getUserFavorite() = dbModule.userDao.loadALl()

    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FavViewModel(db) as T

    }
}