package com.example.weather.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.weather.ui.viewmodel.AppState
import com.example.weather.R
import com.example.weather.databinding.DetailsFragmentBinding
import com.example.weather.model.Weather
import com.example.weather.ui.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment() {
    private var binding: DetailsFragmentBinding? = null

    companion object {
        fun newInstance() = DetailsFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val observer = Observer<AppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getRusWeather()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                val weatherData = appState.weatherData
                binding!!.loadingLayout.visibility = View.GONE
                setData(weatherData[0])
            }
            is AppState.Loading -> {
                binding!!.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding!!.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding!!.mainView, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getRusWeather() }
                    .show()
            }
        }
    }

    private fun setData(weatherData: Weather) {
        binding!!.cityName.text = weatherData.city.city

        val sb = StringBuilder()
        sb.append(getString(R.string.city_coordinates_text))
            .append(weatherData.city.lat.toString())
            .append(", ")
            .append(weatherData.city.lon.toString())
        binding!!.cityCoordinates.text = sb.toString()

        binding!!.temperatureValue.text = weatherData.temperature.toString()
        binding!!.feelsLikeValue.text = weatherData.feelsLike.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}