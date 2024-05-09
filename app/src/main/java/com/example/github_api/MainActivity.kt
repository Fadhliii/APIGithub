package com.example.github_api

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.LiveData
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github_api.API.MODEL.Item
import com.example.github_api.ResultVM.Results
import com.example.github_api.Detailuser.DetailUser
import com.example.github_api.Favorites.FavActivity
import com.example.github_api.Theme.SettingPreferences
import com.example.github_api.Theme.SettingThemeActivity
import com.example.github_api.databinding.ActivityMainBinding

/**
 * Main Activity of the application.
 * This activity is responsible for displaying the list of users and handling user interactions.
 */
class MainActivity : AppCompatActivity() {
    // Binding for this activity
    private lateinit var binding: ActivityMainBinding
    // Adapter for the RecyclerView
    private val adapter by lazy {
        UserAdapter { user ->
            // When a user is clicked, start the DetailUser activity
            intent = Intent(this, DetailUser::class.java).apply {
                putExtra("item", user)
                startActivity(this)
            }
        }
    }
    // ViewModel for this activity
    private val viewModel by viewModels<MainViewModel>() {
        MainViewModel.Factory(SettingPreferences(this))
    }

    /**
     * Called when the activity is starting.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Observe the theme LiveData from the ViewModel
        viewModel.getTheme().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Setup the RecyclerView
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)

        // Setup the SearchView
        binding.searchView.setOnQueryTextListener(object :
                OnQueryTextListener {
            override fun onQueryTextSubmit(search: String?): Boolean {
                binding.progressBar.visibility = View.VISIBLE
                viewModel.getUser(search.toString())
                return true
            }

            override fun onQueryTextChange(search: String?): Boolean = false
        })

        // Observe the Results LiveData from the ViewModel
        viewModel.resultUser.observe(this) {
            when (it) {
                is Results.Success<*> -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.setData(it.data as MutableList<Item>)
                }

                is Results.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, it.exception.message, Toast.LENGTH_SHORT).show()
                }

                is Results.Loading -> {
                    binding.progressBar.visibility = if (it.isLoading) View.VISIBLE else View.GONE
                }
            }
        }

        // Get data from API
        viewModel.getUser()
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Favorite_ic -> {
                // Start the FavActivity when the favorite icon is clicked
                Intent(this, FavActivity::class.java).apply {
                    startActivity(this)
                }
            }

            R.id.theme -> {
                // Start the SettingThemeActivity when the theme icon is clicked
                Intent(this, SettingThemeActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}