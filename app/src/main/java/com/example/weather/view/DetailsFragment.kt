package com.example.weather.view

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.DetailsFragmentBinding
import com.example.weather.model.Weather
import com.example.weather.model.WeatherDTO
import com.example.weather.ui.viewmodel.DetailsViewModel
import com.example.weather.ui.viewmodel.LoadOneCityState
import com.example.weather.utils.*
import com.example.weather.ui.viewmodel.WeatherLoader

class DetailsFragment : Fragment() {
    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var weatherBundle: Weather
    private val onLoadListener : WeatherLoader.WeatherLoaderListener = object:WeatherLoader.WeatherLoaderListener{
        override fun onLoaded(weatherDTO: WeatherDTO) {
            displayWeather(weatherDTO)
        }

        override fun onFailed(throwable: Throwable) {
            TODO("Not yet implemented")
        }
    }

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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        weatherBundle = arguments?.getParcelable(BUNDLE_EXTRA_KEY)
            ?: getWeatherFromViewModel() //загрузка погоды города по умолчанию
        //setWeather(weather)
        val loader = WeatherLoader(onLoadListener, weatherBundle.city.lat, weatherBundle.city.lon)
        loader.loadWeather()
    }

    private fun setWeather(weather: Weather?) {
        weather?.apply {
            val city = weather.city
            with(binding) {
                cityName.text = city.name
                cityCoordinates.text =
                    "${getString(R.string.city_coordinates_text)} ${city.lat}, ${city.lon}"
                temperatureValue.text = weather.temperature.toString()
                feelsLikeValue.text = weather.feelsLike.toString()
                weatherCondition.text = weather.condition
                windSpeedValue.text = weather.windSpeed.toString()
                windDirValue.text = weather.windDir
                pressureValue.text = weather.pressureMm.toString()
                humidityValue.text = weather.humidity.toString()
            }
        }
    }



    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.isVisible = true
            loadingLayout.isVisible = false
            val city = weatherBundle.city
            cityName.text = city.name
            cityCoordinates.text =
                "${getString(R.string.city_coordinates_text)} ${city.lat}, ${city.lon}"
            weatherDTO.apply {
                weatherCondition.text = fact?.condition
                temperatureValue.text = fact?.temp.toString()
                feelsLikeValue.text = fact?.feels_like.toString()
                windSpeedValue.text = fact?.wind_speed.toString()
                windDirValue.text = fact?.wind_dir
                pressureValue.text = fact?.pressure_mm.toString()
                humidityValue.text = fact?.humidity.toString()
            }
        }
    }

    private fun getWeatherFromViewModel(): Weather {
        var weather = Weather()
        viewModel.getLiveAppStateValue().observe(viewLifecycleOwner, { appState ->
            when (appState) {
                is LoadOneCityState.Success -> {
                    binding.loadingLayout.isVisible = false
                    weather = appState.weatherDataMos
                    //setWeather(weather)
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