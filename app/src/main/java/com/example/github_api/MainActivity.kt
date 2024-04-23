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


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy {
        UserAdapter { user ->
            intent = Intent(this, DetailUser::class.java).apply {
                putExtra("item", user)
                startActivity(this)
            }
        }
    }
    private val viewModel by viewModels<MainViewModel>() {
        MainViewModel.Factory(SettingPreferences(this))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getTheme().observe(this) {

            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // Set the adapter
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.setHasFixedSize(true)
        binding.searchView.setOnQueryTextListener(object :
            OnQueryTextListener {
            override fun onQueryTextSubmit(search: String?): Boolean {
                binding.progressBar.visibility = View.VISIBLE
                viewModel.getUser(search.toString())
                return true
            }

            override fun onQueryTextChange(search: String?): Boolean = false

        })

        // Observe the Results LiveData
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.Favorite_ic -> {
                Intent(this, FavActivity::class.java).apply {
                    startActivity(this)
                }
            }

            R.id.theme -> {
//                toast clicked
                Intent(this, SettingThemeActivity::class.java).apply {
                    startActivity(this)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
