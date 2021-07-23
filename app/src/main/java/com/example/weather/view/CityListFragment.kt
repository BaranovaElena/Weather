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
import com.example.weather.databinding.FragmentCityListBinding
import com.example.weather.model.Weather
import com.example.weather.ui.viewmodel.AppStateLoadAll
import com.example.weather.ui.viewmodel.CityListViewModel
import com.example.weather.utils.*

class CityListFragment : Fragment() {
    private var _binding: FragmentCityListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CityListViewModel by lazy {
        ViewModelProvider(this).get(CityListViewModel::class.java)
    }

    private val rusAdapter = CityListAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            setBundleToDetailsFragment(weather)
        }
    })

    private val worldAdapter = CityListAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            setBundleToDetailsFragment(weather)
        }
    })

    private fun setBundleToDetailsFragment(weather: Weather) {
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            with(this) {
                add(R.id.fragment_container, DetailsFragment.newInstance(Bundle().apply {
                    putParcelable(DetailsFragment.BUNDLE_EXTRA_KEY, weather)
                }))
                addToBackStack(null)
                commitAllowingStateLoss()
            }
        }
    }

    companion object {
        fun newInstance() = CityListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cityListRusRecyclerView.adapter = rusAdapter
        binding.cityListWorldRecyclerView.adapter = worldAdapter
        viewModel.getLiveAppStateValue().observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getWeather()
    }

    private fun renderData(loadAllCitiesState: LoadAllCitiesState) {
        when (loadAllCitiesState) {
            is LoadAllCitiesState.Success -> {
                binding.cityListFragmentLoadingLayout.isVisible = false
                rusAdapter.setWeather(loadAllCitiesState.weatherDataRus)
                worldAdapter.setWeather(loadAllCitiesState.weatherDataWorld)
            }
            is LoadAllCitiesState.Loading -> {
                binding.cityListFragmentLoadingLayout.isVisible = true
            }
            is LoadAllCitiesState.Error -> {
                binding.cityListFragmentLoadingLayout.isVisible = false
                binding.cityListFragmentLoadingLayout.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getWeather() }
                )
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        rusAdapter.removeListener()
        super.onDestroy()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }
}
