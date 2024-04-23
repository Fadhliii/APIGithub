package com.example.github_api

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.github_api.API.CLIENT.ApiClient
import com.example.github_api.ResultVM.Results
import com.example.github_api.Theme.SettingPreferences
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

import kotlinx.coroutines.launch


class MainViewModel(private val preferences: SettingPreferences) : ViewModel() {

    val resultUser = MutableLiveData<Results>()
    fun getTheme() = preferences.getThemeSetting().asLiveData()

    fun getUser() {
        viewModelScope.launch {
            flow {
                val response = ApiClient.githubservice.getUser()
                emit(response)
            }
                .onCompletion {
                    resultUser.value = Results.Loading(false)
                }
                .onStart {
                    resultUser.value = Results.Loading(true)
                }
                .catch {
                    Log.e("Error", it.message.toString())
                    it.printStackTrace()
                    resultUser.value = Results.Error(it)
                }.collect {
                    resultUser.value = Results.Success(it)
                }
        }
    }

    fun getUser(username :String) {
        viewModelScope.launch {
            flow {
                val response = ApiClient.githubservice.searchUser(
                    mapOf<String, Any>("q" to username,
                        "per_page" to 10)
                )
                emit(response)
            }
                .onCompletion {
                    resultUser.value = Results.Loading(false)
                }
                .onStart {
                    resultUser.value = Results.Loading(true)
                }
                .catch {
                    Log.e("Error", it.message.toString())
                    it.printStackTrace()
                    resultUser.value = Results.Error(it)
                }.collect {
                    resultUser.value = Results.Success(it.items)
                }
        }
    }

    class Factory(private val pref : SettingPreferences) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(pref) as T
    }

}
