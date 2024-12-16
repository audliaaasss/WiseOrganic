package com.dicoding.wiseorganic.ui.main.analysis

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.dicoding.wiseorganic.R
import com.dicoding.wiseorganic.databinding.FragmentAnalysisBinding

class AnalysisFragment : Fragment() {

    private lateinit var binding: FragmentAnalysisBinding
    private lateinit var viewModel: AnalysisViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAnalysisBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(AnalysisViewModel::class.java)

        loadFirstChart()
        loadSecondChart()
    }

    private fun loadFirstChart() {
        val chartBitmap = BitmapFactory.decodeResource(resources, R.drawable.first_chart)
        binding.firstChartImageView.setImageBitmap(chartBitmap)
    }

    private fun loadSecondChart() {
        val chartBitmap = BitmapFactory.decodeResource(resources, R.drawable.second_chart)
        binding.secondChartImageView.setImageBitmap(chartBitmap)
    }
}