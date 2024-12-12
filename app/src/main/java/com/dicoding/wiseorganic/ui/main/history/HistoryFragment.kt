package com.dicoding.wiseorganic.ui.main.history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.wiseorganic.R
import com.dicoding.wiseorganic.data.auth.UserPreference
import com.dicoding.wiseorganic.data.retrofit.ApiConfig
import com.dicoding.wiseorganic.databinding.ActivityMainBinding
import com.dicoding.wiseorganic.databinding.FragmentHistoryBinding
import com.dicoding.wiseorganic.ui.factory.HistoryViewModelFactory
import com.dicoding.wiseorganic.ui.factory.ViewModelFactory
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlin.concurrent.thread

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: ListHistoryAdapter
    private val list = ArrayList<History>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListHistoryAdapter(list)
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter

        val apiService = ApiConfig.getApiService()
        val userPreference = UserPreference(requireContext())
        val factory = HistoryViewModelFactory(apiService, userPreference)
        val viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        // Observe data dari ViewModel
        viewModel.history.observe(viewLifecycleOwner) { response ->
            if (response.success == true) {
                list.clear()
                response.data?.let { data ->
                    list.add(
                        History(
                            departementName = data.departement?.departementName ?: "Unknown Department",
                            created = data.createdAt ?: "N/A",
                            updated = data.updatedAt ?: "N/A"
                        )
                    )
                }
                adapter.notifyDataSetChanged()
            } else {
                // Tampilkan pesan error yang lebih informatif
                Toast.makeText(
                    requireContext(),
                    response.message ?: "Failed to fetch history",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Observe loading state
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Set up item click callback
        adapter.setOnItemClickCallback(object : ListHistoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: History) {
                showSelectedHistory(data)
            }
        })

        viewLifecycleOwner.lifecycleScope.launch {
            fetchHistory(userPreference, viewModel)
        }
    }

    private suspend fun fetchHistory(userPreference: UserPreference, viewModel: HistoryViewModel) {
        val departmentId = userPreference.departmentId.firstOrNull()

        if (departmentId != null) {
            // Fetch history using the departmentId
            viewModel.fetchHistory(departmentId)
        } else {
            Toast.makeText(requireContext(), "No department ID found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showSelectedHistory(history: History) {
        Toast.makeText(requireContext(), "Kamu memilih " + history, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}