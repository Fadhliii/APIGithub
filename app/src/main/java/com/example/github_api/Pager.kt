package com.example.github_api

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class Pager(
    frag: FragmentActivity,
    private val fragment: MutableList<Fragment>
) : FragmentStateAdapter(frag) {
    override fun getItemCount(): Int {
        return fragment.size
    }

    override fun createFragment(position: Int): Fragment = fragment[position]
}

