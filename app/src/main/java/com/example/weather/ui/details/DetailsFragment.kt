package com.example.weather.ui.details

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.DetailsFragmentBinding
import com.example.weather.domain.model.City
import com.example.weather.domain.model.WeatherDTO
import com.example.weather.domain.repo.city.GPS_PERMISSION_DENIED_EXCEPTION
import com.example.weather.domain.repo.city.GPS_PERMISSION_REQUEST_CODE
import com.example.weather.domain.repo.city.GPS_TURNED_OFF_EXCEPTION
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
        const val BUNDLE_EXTRA_KEY = "CITY_BUNDLE_EXTRA_KEY"

        fun newInstance(city: City?): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle()
            args.putParcelable(BUNDLE_EXTRA_KEY, city)
            fragment.arguments = args
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
        viewModel.cityLoadState.observe(viewLifecycleOwner, { onCityLoad(it) })
        viewModel.weatherLoadState.observe(viewLifecycleOwner, { onWeatherLoad(it) })

        arguments?.getParcelable<City?>(BUNDLE_EXTRA_KEY)?.let {
            cityBundle = it
            viewModel.getWeather(cityBundle)
        } ?: run {
            cityBundle = City()
            viewModel.getDefaultCity(requireContext())
        }
    }

    private fun onCityLoad(state: LoadCityState) {
        when (state) {
            is LoadCityState.Success -> {
                cityBundle = state.loadedCity
                viewModel.getWeather(state.loadedCity)
            }
            is LoadCityState.Loading -> {
                binding.loadingLayout.isVisible = true
            }
            is LoadCityState.Error -> {
                when (state.error.message) {
                    GPS_PERMISSION_DENIED_EXCEPTION -> {
                        requestPermissions(
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            GPS_PERMISSION_REQUEST_CODE
                        )
                    }
                    GPS_TURNED_OFF_EXCEPTION -> {
                        showErrorDialog(
                            getString(R.string.dialog_title_gps_turned_off),
                            getString(R.string.dialog_message_last_location_unknown)
                        )
                    }
                    else -> {
                        binding.loadingLayout.isVisible = false
                        binding.loadingLayout.showSnackBar(
                            getString(R.string.error),
                            getString(R.string.reload),
                            { viewModel.getDefaultCity(requireContext()) }
                        )
                    }
                }
            }
        }
    }

    private fun onWeatherLoad(state: LoadOneWeatherState) {
        when (state) {
            is LoadOneWeatherState.Success -> {
                binding.loadingLayout.isVisible = false
                displayWeather(state.loadedWeather)
            }
            is LoadOneWeatherState.Loading -> {
                binding.loadingLayout.isVisible = true
            }
            is LoadOneWeatherState.Error -> {
                binding.loadingLayout.isVisible = false
                binding.loadingLayout.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getWeather(cityBundle) }
                )
            }
        }
    }

    private fun displayWeather(weatherDTO: WeatherDTO) {
        with(binding) {
            mainView.isVisible = true
            loadingLayout.isVisible = false
            cityName.text = cityBundle.name
            cityCoordinates.text =
                "${getString(R.string.city_coordinates_text)} ${cityBundle.lat}, ${cityBundle.lon}"
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == GPS_PERMISSION_REQUEST_CODE) {
            val pos = permissions.indexOf(Manifest.permission.ACCESS_FINE_LOCATION)
            try {
                if (grantResults[pos] == PackageManager.PERMISSION_GRANTED) {
                    viewModel.getDefaultCity(requireContext())
                }
            } catch (e: IndexOutOfBoundsException) {
                showErrorDialog(
                    getString(R.string.dialog_title_no_gps),
                    getString(R.string.dialog_message_no_gps)
                )
            }
        }
    }

    private fun showErrorDialog(title: String, msg: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(msg)
                .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}