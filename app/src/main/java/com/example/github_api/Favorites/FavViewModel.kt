package com.example.github_api.Favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.github_api.local.DbModule

class FavViewModel(private val dbModule: DbModule) :ViewModel() {


    fun getUserFavorite() = dbModule.userDao.loadALl()

    class Factory(private val db: DbModule) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T = FavViewModel(db) as T

    }
}