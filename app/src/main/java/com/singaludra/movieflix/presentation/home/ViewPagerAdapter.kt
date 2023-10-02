package com.singaludra.movieflix.presentation.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        //TODO: add fragment once fragment is added
        return when (position) {
            0 -> return Fragment()
            1 -> return Fragment()
            else -> Fragment()
        }
    }

    companion object {
        private const val NUM_TABS = 2
    }
}