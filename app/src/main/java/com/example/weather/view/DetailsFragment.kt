package com.example.weather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.DetailsFragmentBinding
import com.example.weather.model.Weather
import com.example.weather.ui.viewmodel.DetailsViewModel
import com.example.weather.ui.viewmodel.LoadOneCityState
import com.example.weather.utils.*

class DetailsFragment : Fragment() {
    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

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
        _binding = DetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weather = arguments?.getParcelable(BUNDLE_EXTRA_KEY)
            ?: getWeatherFromViewModel() //загрузка погоды города по умолчанию
        setWeather(weather)
    }

    private fun setWeather(weather: Weather?) {
        if (weather != null) {
            val city = weather.city
            binding.cityName.text = city.name
            binding.cityCoordinates.text =
                "${getString(R.string.city_coordinates_text)}${city.lat}, ${city.lon}"
            binding.temperatureValue.text = weather.temperature.toString()
            binding.feelsLikeValue.text = weather.feelsLike.toString()
            binding.weatherCondition.text = weather.condition
            binding.windSpeedValue.text = weather.windSpeed.toString()
            binding.windDirValue.text = weather.windDir
            binding.pressureValue.text = weather.pressureMm.toString()
            binding.humidityValue.text = weather.humidity.toString()
        }
    }

    private fun getWeatherFromViewModel(): Weather? {
        var weather: Weather? = null
        viewModel.getLiveAppStateValue().observe(viewLifecycleOwner, { appState ->
            when (appState) {
                is LoadOneCityState.Success -> {
                    binding.loadingLayout.isVisible = false
                    weather = appState.weatherDataMos
                    setWeather(weather)
                }
                is LoadOneCityState.Loading -> {
                    binding.loadingLayout.isVisible = true
                }
                is LoadOneCityState.Error -> {
                    binding.loadingLayout.isVisible = false
                    binding.loadingLayout.showSnackBar(
                        getString(R.string.error),
                        getString(R.string.reload),
                        { viewModel.getWeather() }
                    )
                }
            }
        })
        viewModel.getWeather()
        return weather
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}