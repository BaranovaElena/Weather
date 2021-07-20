package com.example.weather.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weather.R
import com.example.weather.databinding.DetailsFragmentBinding
import com.example.weather.model.Weather

class DetailsFragment : Fragment() {
    private var binding: DetailsFragmentBinding? = null

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

        val weather = arguments?.getParcelable<Weather>(BUNDLE_EXTRA_KEY)
        if (weather != null) {
            val city = weather.city
            binding!!.cityName.text = city.name
            binding!!.cityCoordinates.text =
                "${getString(R.string.city_coordinates_text)}${city.lat}, ${city.lon}"
            binding!!.temperatureValue.text = weather.temperature.toString()
            binding!!.feelsLikeValue.text = weather.feelsLike.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}