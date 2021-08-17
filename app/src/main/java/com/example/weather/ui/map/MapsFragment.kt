package com.example.weather.ui.map

import android.app.AlertDialog
import android.location.Geocoder
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
    private lateinit var map: GoogleMap
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

        binding.mapSearchButton.setOnClickListener {
            binding.mapSearchEditText.text?.apply {
                setMarkerToLocation(this.toString())
            }
        }
    }

    private val callback = OnMapReadyCallback { readyMap ->
        this.map = readyMap
        val defaultLatLng = LatLng(55.755826, 37.617299)
        loadLocationOnMap(defaultLatLng, 2.5f)

        map.setOnMapClickListener {
            loadLocationOnMap(it, 1.0f)
        }
    }

    private fun loadLocationOnMap(latLng: LatLng, zoomFactor: Float) {
        map.apply {
            clear()
            currentLatLng = latLng
            addMarker(MarkerOptions().position(latLng))
            moveCamera(
                CameraUpdateFactory.newLatLngZoom(latLng, map.cameraPosition.zoom * zoomFactor)
            )
            viewModel.getAddress(requireContext(), latLng)
        }
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

    private fun setMarkerToLocation(address: String) {
        binding.mapShowWeatherButton.isEnabled = false
        Thread {
            try {
                Geocoder(context).getFromLocationName(address, 1)[0]?.let {
                    requireActivity().runOnUiThread {
                        binding.mapSearchButton.isEnabled = true
                        loadLocationOnMap(LatLng(it.latitude, it.longitude), 2.0f)
                    }
                }
            } catch (e: Exception) {
                requireActivity().runOnUiThread {
                    AlertDialog.Builder(context)
                        .setTitle(getString(R.string.error))
                        .setMessage(getString(R.string.map_wrong_input_address))
                        .setNegativeButton(getString(R.string.dialog_button_close)) { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
            }
        }.start()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}