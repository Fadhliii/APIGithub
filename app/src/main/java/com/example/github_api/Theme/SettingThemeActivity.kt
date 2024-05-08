package com.example.github_api.Theme


import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.example.github_api.R
import com.example.github_api.databinding.ActivitySettingThemeBinding

class SettingThemeActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySettingThemeBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<ThemeViewModel> {
        ThemeViewModel.Factory(
            SettingPreferences(
                this
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        viewmodel to get the theme
        viewModel.getTheme().observe(this) { isDarkMode ->
            binding.switch1.text = if (isDarkMode) "Dark Mode" else "Light Mode"
            AppCompatDelegate.setDefaultNightMode(if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            binding.switch1.isChecked = isDarkMode
        }
//        set the switch to change the theme
        binding.switch1.setOnCheckedChangeListener { _, isChecked -> viewModel.saveTheme(isChecked) }
    }

//    back button
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            finish(); true
        }
        else -> super.onOptionsItemSelected(item)
    }
}