package com.example.weather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.weather.R
import com.example.weather.databinding.MainActivityBinding
import com.example.weather.ui.citylist.CityListFragment
import com.example.weather.ui.contacts.ContactsFragment
import com.example.weather.ui.details.DetailsFragment
import com.example.weather.ui.favorites.FavoritesFragment
import com.example.weather.ui.history.HistoryFragment
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
                        putParcelable(DetailsFragment.BUNDLE_EXTRA_KEY, null)
                    }))
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_menu_contacts -> {
                supportFragmentManager.beginTransaction()
                    .add(R.id.fragment_container, ContactsFragment.newInstance())
                    .addToBackStack(null)
                    .commitAllowingStateLoss()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}