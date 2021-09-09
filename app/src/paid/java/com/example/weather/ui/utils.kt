package com.example.weather.ui

import android.view.MenuItem
import com.example.weather.R
import com.example.weather.ui.citylist.CityListFragment
import com.example.weather.ui.details.DetailsFragment
import com.example.weather.ui.favorites.FavoritesFragment
import com.example.weather.ui.history.HistoryFragment

fun MainActivity.setBottomNavListener(item: MenuItem): Boolean {
    when (item.itemId) {
        R.id.navigation_list -> {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CityListFragment.newInstance())
                .commitNow()
        }
        R.id.navigation_default -> {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DetailsFragment.newInstance(null))
                .commitNow()
        }
        R.id.navigation_favorites -> {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FavoritesFragment.newInstance())
                .commitNow()
        }
        R.id.navigation_history -> {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HistoryFragment.newInstance())
                .commitNow()
        }
    }
    return true
}