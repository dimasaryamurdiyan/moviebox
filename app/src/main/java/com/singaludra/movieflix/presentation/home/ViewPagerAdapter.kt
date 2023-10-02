package com.singaludra.movieflix.presentation.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.singaludra.movieflix.presentation.favorite.FavoriteFragment
import com.singaludra.movieflix.presentation.movies.MovieFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        //TODO: add fragment once fragment is added
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