package com.example.weather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.weather.R
import com.example.weather.databinding.MainActivityBinding
import com.example.weather.domain.model.City
import com.example.weather.ui.citylist.CityListFragment
import com.example.weather.ui.details.DetailsFragment
import com.example.weather.ui.favorites.FavoritesFragment
import com.example.weather.ui.history.HistoryFragment
import com.example.weather.ui.map.MapsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), CityListFragment.Controller {
    private lateinit var binding: MainActivityBinding
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = binding.navView
        bottomNavigationView?.setOnItemSelectedListener { item -> setBottomNavListener(item) }
    }

    private fun setBottomNavListener(item: MenuItem): Boolean {
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

    override fun openDetailsScreen(city: City) {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, DetailsFragment.newInstance(city))
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }

    override fun openMap() {
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, MapsFragment.newInstance())
            .addToBackStack(null)
            .commitAllowingStateLoss()
    }
}