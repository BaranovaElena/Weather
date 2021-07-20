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

    private val adapter = CityListAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
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
    })

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
        binding!!.cityListRecyclerView.adapter = adapter
        binding!!.cityListFragmentFloatingButton.setOnClickListener { changeWeatherDataSet() }
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getRusWeather()
    }

    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWorldWeather()
            binding!!.cityListFragmentFloatingButton.setImageResource(R.drawable.ic_globe)
        } else {
            viewModel.getRusWeather()
            binding!!.cityListFragmentFloatingButton.setImageResource(R.drawable.russia_flag)
        }
        isDataSetRus = !isDataSetRus
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                binding!!.cityListFragmentLoadingLayout.isVisible = false
                adapter.setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding!!.cityListFragmentLoadingLayout.isVisible = true
            }
            is AppState.Error -> {
                binding!!.cityListFragmentLoadingLayout.isVisible = false
                Snackbar.make(
                    binding!!.cityListFragmentFloatingButton,
                    getString(R.string.error),
                    Snackbar.LENGTH_INDEFINITE
                ).setAction(getString(R.string.reload)) {
                    viewModel.getRusWeather()
                }.show()
            }
        }
    }

    override fun onDestroy() {
        binding = null
        adapter.removeListener()
        super.onDestroy()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }
}
