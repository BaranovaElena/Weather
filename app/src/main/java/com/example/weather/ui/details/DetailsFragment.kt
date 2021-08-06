package com.example.weather.ui.details

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.DetailsFragmentBinding
import com.example.weather.domain.model.City
import com.example.weather.domain.model.WeatherDTO
import com.example.weather.utils.*
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou

class DetailsFragment : Fragment() {
    private var _binding: DetailsFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var cityBundle: City

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
        cityBundle = arguments?.getParcelable(BUNDLE_EXTRA_KEY)
            ?: viewModel.getDefaultCity()
        viewModel.liveLoadStateValue.observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getWeather(cityBundle.lat, cityBundle.lon)
    }

    private fun renderData(state: LoadOneCityState) {
        when (state) {
            is LoadOneCityState.Success -> {
                binding.loadingLayout.isVisible = false
                displayWeather(state.loadedWeather)
            }
            is LoadOneCityState.Loading -> {
                binding.loadingLayout.isVisible = true
            }
            is LoadOneCityState.Error -> {
                binding.loadingLayout.isVisible = false
                binding.loadingLayout.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getWeather(cityBundle.lat, cityBundle.lon) }
                )
            }
        }
    }

    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.isVisible = true
            loadingLayout.isVisible = false
            val city = cityBundle
            cityName.text = city.name
            cityCoordinates.text =
                "${getString(R.string.city_coordinates_text)} ${city.lat}, ${city.lon}"
            weatherDTO.apply {
                weatherCondition.text = fact?.condition
                temperatureValue.text = fact?.temperature.toString()
                feelsLikeValue.text = fact?.feelsLike.toString()
                windSpeedValue.text = fact?.windSpeed.toString()
                windDirValue.text = fact?.windDir
                pressureValue.text = fact?.pressureMm.toString()
                humidityValue.text = fact?.humidity.toString()
                fact?.icon?.let {
                    GlideToVectorYou.justLoadImage(
                        activity,
                        Uri.parse(it),
                        weatherConditionIcon
                    )
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}