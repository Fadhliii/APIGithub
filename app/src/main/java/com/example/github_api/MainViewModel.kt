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

/**
 * ViewModel for the Main Activity.
 * This ViewModel is responsible for fetching data from the Github API and providing it to the Main Activity.
 *
 * @property preferences The SettingPreferences instance used to get the theme setting.
 * @property resultUser The LiveData that holds the result of the API call.
 */
class MainViewModel(private val preferences: SettingPreferences) : ViewModel() {

    val resultUser = MutableLiveData<Results>()

    /**
     * Get the theme setting from the preferences.
     * This function is used to get the theme setting from the preferences.
     * @return LiveData<Boolean> The theme setting.
     */
    fun getTheme() = preferences.getThemeSetting().asLiveData()

    /**---------------------------------------------------------------------------------------------
     * Fetch the list of users from the Github API.
     * This function is used to get the list of users.
     * The list is limited to 10 results.
     */
    fun getUser() {
        // Use the viewModelScope to launch a coroutine in the ViewModel.
        viewModelScope.launch {
            // Use the flow builder to create a flow that emits the result of the API call.
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

    /**
     * Fetch the list of users from the Github API based on the username.
     * This function is used to search for a specific user.
     * The search is done based on the username.
     * The search is limited to 10 results.
     * @param username The username to search for.
     */
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

    /**
     * Factory for creating MainViewModel instances.
     * Factory is used to pass the SettingPreferences instance to the MainViewModel.
     * This is needed because the ViewModel constructor cannot take any parameters.
     * @property pref The SettingPreferences instance used to get the theme setting.
     */
    class Factory(private val pref : SettingPreferences) : ViewModelProvider.NewInstanceFactory(){
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(pref) as T
    }
}