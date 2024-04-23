package com.example.github_api.Detailuser

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import coil.transform.CircleCropTransformation
import com.example.github_api.API.MODEL.DetailUserGithub
import com.example.github_api.API.MODEL.Item
import com.example.github_api.Detailuser.follower_ing.FollowFragment
import com.example.github_api.Pager
import com.example.github_api.R
import com.example.github_api.ResultVM.Results
import com.example.github_api.databinding.ActivityDetailUserBinding
import com.example.github_api.local.DbModule
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUser : AppCompatActivity() {
    private lateinit var binding: ActivityDetailUserBinding
    private val viewModel by viewModels<DetailViewModel>{
        DetailViewModel.Factory(DbModule(this))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        set content view binding to activity detail user binding layout
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

//        get username from main activity
        val item: Item? = intent.getParcelableExtra<Item>("item")
        val username = item?.login ?: ""

        viewModel.getDetailUser(username)
        viewModel.resultDetailUser.observe(this) {
            when (it) {
                is Results.Success<*> -> {
                    val user = it.data as DetailUserGithub
                    binding.imguser.load(user.avatar_url) {
                        transformations(CircleCropTransformation())
                    }
                    binding.name.text = user.name
                    binding.usrname.text = username
                }

                is Results.Error -> {
                    Toast.makeText(this, it.exception.message, Toast.LENGTH_SHORT).show()
                }

                is Results.Loading -> {
                    binding.progressBar.visibility = if (it.isLoading) View.VISIBLE else View.GONE
                }
            }
        }
        viewModel.getDetailUser(username)


        viewModel.resultSuccess.observe(this){
            binding.BtnFavorite.changeIconColor(R.color.red)
        }
        viewModel.resultFailFav.observe(this){
            binding.BtnFavorite.changeIconColor(R.color.white)
        }


        binding.BtnFavorite.setOnClickListener{
            viewModel.setFav(item)

        }

        viewModel.findFavorite(item?.id ?: 0){
            binding.BtnFavorite.changeIconColor(R.color.red)

        }

//        create tab layout
        val fragments = mutableListOf<Fragment>(
            FollowFragment.newInstance(FollowFragment.Followers_index),
            FollowFragment.newInstance(FollowFragment.Following_index)
        )
        val tittleFragments = mutableListOf(
            getString(R.string.followers),
            getString(R.string.following)
        )
//        set adapter
        val adapter = Pager(this, fragments)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tableLayout, binding.viewPager) { tab, position ->
            tab.text = tittleFragments[position]
        }.attach()

//        get followers and following count
        binding.tableLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.position == 0){
                    viewModel.getFollowers(username)
            } else{
                viewModel.getFollowing(username)
            }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
        // create following count
        viewModel.resultFollowing.observe(this){
            when(it){
                is Results.Success<*> -> {
                    val data = it.data as MutableList<Item> // Assuming Followers is the correct class
                    binding.tableLayout.getTabAt(1)?.text = "${getString(R.string.following)} (${data.size})"
                }
                is Results.Error -> {
                    // Handle error case
                }
                is Results.Loading -> {
                    // Handle loading case
                }
            }
            viewModel.resultFollowers.observe(this){
                when(it){
                    is Results.Success<*> -> {
                        val data = it.data as MutableList<Item> // Assuming Followers is the correct class
                        binding.tableLayout.getTabAt(0)?.text = "${getString(R.string.followers)} (${data.size})"
                    }
                    is Results.Error -> {
                        // Handle error case
                    }
                    is Results.Loading -> {
                        // Handle loading case
                    }
                }
            }
        }

    }

}

fun FloatingActionButton.changeIconColor(@ColorRes color :Int) {

    imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this.context, color))
}
