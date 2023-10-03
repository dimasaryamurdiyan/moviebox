package com.singaludra.movieflix.feature.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.singaludra.movieflix.feature.favorite.FavoriteFragment
import com.singaludra.movieflix.feature.movies.MovieFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> return MovieFragment()
            1 -> return FavoriteFragment()
            else -> MovieFragment()
        }
    }

    companion object {
        private const val NUM_TABS = 2
    }
}