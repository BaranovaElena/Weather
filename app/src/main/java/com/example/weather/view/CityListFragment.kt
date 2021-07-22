package com.example.weather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentCityListBinding
import com.example.weather.model.Weather
import com.example.weather.ui.viewmodel.AppState
import com.example.weather.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class CityListFragment : Fragment() {
    private var binding: FragmentCityListBinding? = null

    private lateinit var viewModel: MainViewModel

    private val rusAdapter = CityListAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            setBundleToDetailsFragment(weather)
        }
    })

    private val worldAdapter = CityListAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            setBundleToDetailsFragment(weather)
        }
    })

    private fun setBundleToDetailsFragment(weather: Weather) {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            val bundle = Bundle()
            bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA_KEY, weather)
            with(transaction) {
                add(R.id.container, DetailsFragment.newInstance(bundle))
                addToBackStack(null)
                commitAllowingStateLoss()
            }
        }
    }

    private var isDataSetRus: Boolean = true

    companion object {
        fun newInstance() = CityListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCityListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding!!.cityListRusRecyclerView.adapter = rusAdapter
        binding!!.cityListWorldRecyclerView.adapter = worldAdapter
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeather()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding!!.cityListFragmentLoadingLayout.isVisible = false
                rusAdapter.setWeather(appState.weatherDataRus)
                worldAdapter.setWeather(appState.weatherDataWorld)
            }
            is AppState.Loading -> {
                binding!!.cityListFragmentLoadingLayout.isVisible = true
            }
            is AppState.Error -> {
                binding!!.cityListFragmentLoadingLayout.isVisible = false
                Snackbar.make(
                    binding!!.cityListFragmentLoadingLayout,
                    getString(R.string.error),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.reload)) {
                    viewModel.getWeather()
                }.show()
            }
        }
    }

    override fun onDestroy() {
        binding = null
        rusAdapter.removeListener()
        super.onDestroy()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }
}
