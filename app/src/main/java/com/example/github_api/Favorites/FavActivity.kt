package com.example.github_api.Favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github_api.Detailuser.DetailUser
import com.example.github_api.R
import com.example.github_api.UserAdapter
import com.example.github_api.databinding.ActivityFavBinding
import com.example.github_api.local.DbModule
/**
 * ViewModel for the Favorites feature.
 * This ViewModel is responsible for managing the data for the Favorites feature.
 * @property dbModule The database module used to interact with the database.
 */
class FavActivity : AppCompatActivity() {
//    binding the layout
    private lateinit var binding: ActivityFavBinding
    private val adapter by lazy {
//        set the adapter to the recyclerview
        UserAdapter { user ->
            intent = Intent(this, DetailUser::class.java).apply {
                putExtra("item", user)
                startActivity(this)
            }
        }
    }

    //    get the viewmodel from the factory class and observe the data
    private val viewModel by viewModels<FavViewModel> {
        FavViewModel.Factory(DbModule(this))
    }
    //    set the layout and the recyclerview adapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavBinding.inflate(layoutInflater)
        setContentView(binding.root)
//    set the action bar and the back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//    binding the viewmodel to the layout
        binding.FavRv.layoutManager = LinearLayoutManager(this)
        binding.FavRv.adapter = adapter
//        viewmodel to get the data from the database
        viewModel.getUserFavorite().observe(this) {
            adapter.setData(it)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh the data when the activity comes into the foreground
        viewModel.getUserFavorite().observe(this) {
            adapter.setData(it)
        }
    }

    //   set the back button to finish the activity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)

    }
}