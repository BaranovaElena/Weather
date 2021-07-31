package com.example.weather.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.weather.R
import com.example.weather.databinding.MainActivityBinding
import com.example.weather.model.RepoImpl
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
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
                    .replace(R.id.fragment_container, DetailsFragment.newInstance(Bundle().apply {
                        putParcelable(DetailsFragment.BUNDLE_EXTRA_KEY, RepoImpl().getWeatherFromLocalStorageRus()[0])
                    }))
                    .commitNow()
            }
            R.id.navigation_favorites -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FavoritesFragment.newInstance())
                    .commitNow()
            }
        }
        return true
    }
}