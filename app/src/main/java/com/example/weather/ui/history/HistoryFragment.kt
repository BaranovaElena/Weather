package com.example.weather.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentHistoryBinding
import com.example.weather.utils.showSnackBar

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }

    private val adapter = HistoryAdapter()

    companion object {
        fun newInstance() = HistoryFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.historyList.adapter = adapter
        viewModel.liveLoadStateValue.observe(viewLifecycleOwner, { renderData(it) })
        viewModel.getWeather()
    }

    private fun renderData(state: LoadHistoryState) {
        when (state) {
            is LoadHistoryState.Success -> {
                binding.cityListFragmentLoadingLayout.isVisible = false
                adapter.setWeather(state.weatherData)
            }
            is LoadHistoryState.Loading -> {
                binding.cityListFragmentLoadingLayout.isVisible = true
            }
            is LoadHistoryState.Error -> {
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
        super.onDestroy()
    }
}