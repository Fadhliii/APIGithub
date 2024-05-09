package com.example.github_api.Theme

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Extension property to get a preferencesDataStore with the name "settings"
private val Context.prefDataStore by preferencesDataStore("settings")

/**
 * Class for managing theme settings using DataStore.
 * @property settingDataStore The context used to create the DataStore instance.
 * @property themeKey The key used to store the theme setting in the DataStore.
 */
class SettingPreferences constructor(context: Context) {
    // The DataStore instance
    private val settingDataStore = context.prefDataStore
    // The key for the theme setting in the DataStore
    private val themeKey = booleanPreferencesKey("theme_setting")

    /**
     * Get the current theme setting.
     * @param Flow<Boolean> is a stream of boolean values that can be observed.
     * The value will be updated whenever the theme setting changes.
     */
    fun getThemeSetting(): Flow<Boolean> = settingDataStore
        .data.map { preferences ->
            // Get the theme setting from the preferences, default to false if not set
            preferences[themeKey] ?: false
        }

    /**
     * Save the theme setting.
     *
     * @param isDarkModeActive The new theme setting to save.
     */
    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        settingDataStore
            .edit { preferences ->
                // Save the theme setting in the preferences
                preferences[themeKey] = isDarkModeActive
            }
    }
}