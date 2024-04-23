package com.example.github_api.Theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ThemeViewModel(private val pref: SettingPreferences) : ViewModel() {
    fun getTheme() = pref.getThemeSetting().asLiveData()

    fun saveTheme(isDark : Boolean) {
        viewModelScope.launch{
            pref.saveThemeSetting(isDark)
        }
    }
    class Factory(private val pref : SettingPreferences) : ViewModelProvider.NewInstanceFactory(){
        override fun <T : ViewModel> create(modelClass: Class<T>): T = ThemeViewModel(pref) as T
    }

}