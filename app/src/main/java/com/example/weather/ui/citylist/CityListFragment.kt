package com.example.weather.ui.citylist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentCityListBinding
import com.example.weather.domain.model.City
import com.example.weather.utils.*

class CityListFragment : Fragment() {
    private var _binding: FragmentCityListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CityListViewModel by lazy {
        ViewModelProvider(this).get(CityListViewModel::class.java)
    }

    private val localAdapter = CityListAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(city: City) {
            (requireActivity() as Controller).openDetailsScreen(city)
        }
    })

    private val worldAdapter = CityListAdapter(object : OnItemViewClickListener {
        override fun onItemViewClick(city: City) {
            (requireActivity() as Controller).openDetailsScreen(city)
        }
    })

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
        binding.cityListRusRecyclerView.adapter = localAdapter
        binding.cityListWorldRecyclerView.adapter = worldAdapter
        viewModel.loadStateLiveData.observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getCityLists()
    }

    private fun renderData(loadAllCitiesState: LoadAllCitiesState) {
        when (loadAllCitiesState) {
            is LoadAllCitiesState.Success -> {
                binding.cityListFragmentLoadingLayout.isVisible = false
                localAdapter.setCityList(loadAllCitiesState.localCities)
                worldAdapter.setCityList(loadAllCitiesState.worldCities)
            }
            is LoadAllCitiesState.Loading -> {
                binding.cityListFragmentLoadingLayout.isVisible = true
            }
            is LoadAllCitiesState.Error -> {
                binding.cityListFragmentLoadingLayout.isVisible = false
                binding.cityListFragmentLoadingLayout.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload),
                    { viewModel.getCityLists() }
                )
            }
        }
    }

    override fun onDestroy() {
        _binding = null
        localAdapter.removeListener()
        super.onDestroy()
    }

    interface OnItemViewClickListener {
        fun onItemViewClick(city: City)
    }

    interface Controller {
        fun openDetailsScreen(city: City)
    }
}
