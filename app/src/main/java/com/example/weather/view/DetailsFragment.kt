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
import com.example.weather.databinding.DetailsFragmentBinding
import com.example.weather.model.Weather
import com.example.weather.ui.viewmodel.AppStateLoadOne
import com.example.weather.ui.viewmodel.DetailsViewModel
import com.google.android.material.snackbar.Snackbar

class DetailsFragment : Fragment() {
    private var binding: DetailsFragmentBinding? = null
    private lateinit var viewModel: DetailsViewModel

    companion object {
        const val BUNDLE_EXTRA_KEY = "WEATHER_BUNDLE_EXTRA_KEY"

        fun newInstance(bundle: Bundle): DetailsFragment {
            val fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA_KEY)
        if (weather == null) {
            weather = getWeatherFromViewModel() //загрузка погоды города по умолчанию
        }
        setWeather(weather)
    }

    private fun setWeather(weather: Weather?) {
        if (weather != null) {
            val city = weather.city
            binding!!.cityName.text = city.city
            binding!!.cityCoordinates.text =
                "${getString(R.string.city_coordinates_text)}${city.lat}, ${city.lon}"
            binding!!.temperatureValue.text = weather.temperature.toString()
            binding!!.feelsLikeValue.text = weather.feelsLike.toString()
        }
    }

    private fun getWeatherFromViewModel(): Weather? {
        viewModel = ViewModelProvider(this).get(DetailsViewModel::class.java)
        var weather: Weather? = null
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer {
            when (it) {
                is AppStateLoadOne.Success -> {
                    binding!!.loadingLayout.isVisible = false
                    weather = it.weatherDataMos
                    setWeather(weather)
                }
                is AppStateLoadOne.Loading -> {
                    binding!!.loadingLayout.isVisible = true
                }
                is AppStateLoadOne.Error -> {
                    binding!!.loadingLayout.isVisible = false
                    Snackbar.make(
                        binding!!.loadingLayout,
                        getString(R.string.error),
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction(getString(R.string.reload)) {
                        viewModel.getWeather()
                    }.show()
                }
            }
        })
        viewModel.getWeather()
        return weather
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}