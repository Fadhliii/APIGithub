package com.example.github_api.Favorites

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
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

class FavActivity : AppCompatActivity() {

    private lateinit var  binding :ActivityFavBinding

    private val adapter by lazy {
        UserAdapter { user ->
            intent = Intent(this, DetailUser::class.java).apply {
                putExtra("item", user)
                startActivity(this)
            }
        }
    }
    private val viewModel by viewModels<FavViewModel>{
        FavViewModel.Factory(DbModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.FavRv.layoutManager = LinearLayoutManager(this)
        binding.FavRv.adapter = adapter


        viewModel.getUserFavorite().observe(this){
            adapter.setData(it)
        }
//

    }
}