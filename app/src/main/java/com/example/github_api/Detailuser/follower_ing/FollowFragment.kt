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

class FollowFragment : Fragment() {

    private var binding: FragmentFollowBinding? = null
    private val adapter = UserAdapter { }
    private val viewModel by activityViewModels<DetailViewModel>()
    var index = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.rvFollows?.apply {
            layoutManager = LinearLayoutManager(requireActivity())
            setHasFixedSize(true)
            adapter = this@FollowFragment.adapter
        }

        when(index){
            Followers_index -> {
                viewModel.resultFollowers.observe(viewLifecycleOwner){
                    manageResultFollows(it)
                }
            }
            Following_index -> {
                viewModel.resultFollowing.observe(viewLifecycleOwner){
                    manageResultFollows(it)
                }
            }
        }
    }

    private fun manageResultFollows(result: Results) {
        when (result) {
            is Results.Success<*> -> {
                adapter.setData(result.data as MutableList<Item>)
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
        const val Following_index = 100
        const val Followers_index = 101
        fun newInstance(index: Int) = FollowFragment().also { it.index = index }
    }
}