package com.example.weather.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.weather.AppState
import com.example.weather.databinding.MainFragmentBinding
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {
    private var binding: MainFragmentBinding? = null

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val observer = Observer<AppState> { renderData(it) }
        viewModel.getLiveData().observe(viewLifecycleOwner, observer)
        viewModel.getWeather()
    }

    private fun renderData(appState: AppState) {
//        Toast.makeText(context, data.toString(), Toast.LENGTH_LONG).show()
//        binding!!.message.text = data.toString()
        when (appState) {
            is AppState.Success -> {
                val weatherData = appState.weatherData
                binding!!.loadingLayout.visibility = View.GONE
                Snackbar.make(binding!!.mainView, "Success", Snackbar.LENGTH_LONG).show()
            }
            is AppState.Loading -> {
                binding!!.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding!!.loadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding!!.mainView, "Error", Snackbar.LENGTH_INDEFINITE)
                    .setAction("Reload") { viewModel.getWeather() }
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}