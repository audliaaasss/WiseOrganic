package com.dicoding.wiseorganic.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.wiseorganic.data.auth.UserPreference
import com.dicoding.wiseorganic.data.retrofit.ApiService
import com.dicoding.wiseorganic.ui.main.history.HistoryViewModel

class HistoryViewModelFactory(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(apiService, userPreference) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}