package com.example.github_api

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Pager class that extends FragmentStateAdapter.
 * This class is responsible for providing fragments for the ViewPager2.
 * Its to display a list of follower and following users.
 * @property frag The FragmentActivity that the ViewPager2 is part of.
 * @property fragment The list of fragments to be displayed by the ViewPager2.
 */
class Pager(
        frag: FragmentActivity,
        private val fragment: MutableList<Fragment>
) : FragmentStateAdapter(frag) {

    /**
     * Returns the total number of fragments.
     *
     * @return Int The number of fragments.
     */
    override fun getItemCount(): Int {
        return fragment.size
    }

    /**
     * Creates the fragment for the specified position.
     *
     * @param position The position of the fragment to be displayed.
     * @return Fragment The fragment to be displayed.
     */
    override fun createFragment(position: Int): Fragment = fragment[position]
}