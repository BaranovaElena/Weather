package com.example.weather.ui.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentMapsBinding
import com.example.weather.domain.model.City
import com.example.weather.ui.citylist.CityListFragment
import com.example.weather.ui.details.LoadCityState

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var currentLatLng: LatLng

    private val viewModel: MapsViewModel by lazy {
        ViewModelProvider(this).get(MapsViewModel::class.java)
    }

    companion object {
        fun newInstance() = MapsFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.cityLoadState.observe(viewLifecycleOwner, { onCityLoad(it) })
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.mapShowWeatherButton.isEnabled = false
        binding.mapShowWeatherButton.setOnClickListener {
            (requireActivity() as CityListFragment.Controller).openDetailsScreen(
                City(
                    binding.mapAddressTextView.text.toString(),
                    currentLatLng.latitude,
                    currentLatLng.longitude
                )
            )
        }
    }

    private val callback = OnMapReadyCallback { map ->
        setDefaultMap(map)

        map.setOnMapClickListener {
            map.apply {
                clear()
                currentLatLng = it
                addMarker(MarkerOptions().position(it))
                moveCamera(CameraUpdateFactory.newLatLngZoom(it, map.cameraPosition.zoom))
                viewModel.getAddress(requireContext(), it)
            }
        }
    }

    private fun setDefaultMap(map: GoogleMap) {
        val defaultLatLng = LatLng(55.755826, 37.617299)
        currentLatLng = defaultLatLng
        map.addMarker(MarkerOptions().position(defaultLatLng))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 5f))
        viewModel.getAddress(requireContext(), defaultLatLng)
    }

    private fun onCityLoad(state: LoadCityState) {
        when (state) {
            is LoadCityState.Success -> {
                binding.mapAddressTextView.text = state.loadedCity.name
                binding.mapShowWeatherButton.isEnabled = true
            }
            is LoadCityState.Loading -> {
                binding.mapAddressTextView.text = getString(R.string.loading)
                binding.mapShowWeatherButton.isEnabled = false
            }
            is LoadCityState.Error -> {
                binding.mapAddressTextView.text = state.error.message
                binding.mapShowWeatherButton.isEnabled = false
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}