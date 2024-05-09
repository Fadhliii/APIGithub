package com.example.github_api.Detailuser.follower_ing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.github_api.API.MODEL.Item
import com.example.github_api.Detailuser.DetailViewModel
import com.example.github_api.ResultVM.Results
import com.example.github_api.UserAdapter
import com.example.github_api.databinding.FragmentFollowBinding

/**
 * A Fragment representing a list of Followers/Following for a specific user.
 */
class FollowFragment : Fragment() {

    // Binding for this fragment
    private var binding: FragmentFollowBinding? = null
    // Adapter for the RecyclerView
    private val adapter = UserAdapter { }
    // ViewModel for this fragment
    private val viewModel by activityViewModels<DetailViewModel>()
    // Index to determine whether to fetch followers or following data
    var index = 0
    // Username of the user
    var username: String? = null

    /**
     * Inflate the layout for this fragment
     */
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    /**
     * Setup the RecyclerView and fetch the data
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvFollows?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowFragment.adapter
        }

        username?.let {
            when(index){
                Followers_index -> {
                    viewModel.getFollowers(it) // Fetch followers data
                    viewModel.resultFollowers.observe(viewLifecycleOwner){
                        manageResultFollows(it)
                    }
                }
                Following_index -> {
                    viewModel.getFollowing(it) // Fetch following data
                    viewModel.resultFollowing.observe(viewLifecycleOwner){
                        manageResultFollows(it)
                    }
                }
            }
        }
    }

    /**
     * Handle the result from the ViewModel
     */
    private fun manageResultFollows(result: Results) {
        when (result) {
            is Results.Success<*> -> {
                adapter.setData(result.data as List<Item>)
            }
            is Results.Error -> {
                Toast.makeText(requireActivity(), result.exception.message, Toast.LENGTH_SHORT).show()
            }
            is Results.Loading -> {
                binding?.progressBar?.visibility = if (result.isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    companion object {
        // Define the index for the fragment
        const val Following_index = 100
        const val Followers_index = 101

        /**
         * Create a new instance of this fragment
         * instance is created based on the index and username
         */
        fun newInstance(index: Int, username: String) = FollowFragment().also {
            it.index = index
            it.username = username
        }
    }
}