package com.example.weather.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather.R
import com.example.weather.databinding.DetailsFragmentBinding
import com.example.weather.model.City
import com.example.weather.model.WeatherDTO
import com.example.weather.services.DetailsService
import com.example.weather.services.LATITUDE_EXTRA
import com.example.weather.services.LONGITUDE_EXTRA
import com.example.weather.ui.viewmodel.DetailsViewModel
import com.example.weather.ui.viewmodel.LoadOneCityState
import com.example.weather.utils.*

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_WEATHER_DTO_EXTRA = "WEATHER DTO EXTRA"
private const val PROCESS_ERROR = "Обработка ошибки"

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

    private val loadResultsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                DETAILS_INTENT_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_DATA_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_EMPTY_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_URL_MALFORMED_EXTRA -> TODO(PROCESS_ERROR)
                DETAILS_RESPONSE_SUCCESS_EXTRA -> displayWeather(
                    intent.getParcelableExtra(DETAILS_WEATHER_DTO_EXTRA)
                )
                else -> TODO(PROCESS_ERROR)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it)
                .registerReceiver(loadResultsReceiver, IntentFilter(DETAILS_INTENT_FILTER))
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
        context?.startService(Intent(context, DetailsService::class.java).apply {
            putExtra(LATITUDE_EXTRA, cityBundle.lat)
            putExtra(LONGITUDE_EXTRA, cityBundle.lon)
        })
    }

    private fun displayWeather(weatherDTO: WeatherDTO?) {
        with(binding) {
            mainView.isVisible = true
            loadingLayout.isVisible = false

            weatherDTO?.fact?.apply {
                val city = cityBundle
                cityName.text = city.name
                cityCoordinates.text =
                    "${getString(R.string.city_coordinates_text)} ${city.lat}, ${city.lon}"
                weatherCondition.text = condition
                temperatureValue.text = temperature.toString()
                feelsLikeValue.text = feelsLike.toString()
                windSpeedValue.text = windSpeed.toString()
                windDirValue.text = windDir
                pressureValue.text = pressureMm.toString()
                humidityValue.text = humidity.toString()
            }
        }
    }

override fun onDestroyView() {
    context?.let {
        LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
    }
    _binding = null
    super.onDestroyView()
}
}