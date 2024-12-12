package com.dicoding.wiseorganic.ui.main.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.wiseorganic.data.auth.UserPreference
import com.dicoding.wiseorganic.data.response.WasteResponse
import com.dicoding.wiseorganic.data.retrofit.ApiConfig
import com.dicoding.wiseorganic.data.retrofit.ApiService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val apiService: ApiService,
    private val userPreference: UserPreference
) : ViewModel() {

    private val _history = MutableLiveData<WasteResponse>()
    val history: LiveData<WasteResponse> get() = _history

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchHistory(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val userToken = getUserToken()
                val departmentId = getDepartmentId()

                if (userToken.isNullOrEmpty()) {
                    _history.value = WasteResponse(
                        success = false,
                        message = "Authentication token is required"
                    )
                    return@launch
                }

                if (departmentId == null) {
                    _history.value = WasteResponse(
                        success = false,
                        message = "Department ID is required"
                    )
                    return@launch
                }

                val token = "Bearer $userToken"
                val response = apiService.history(token, id)
                _history.value = response

                println("Fetched History Data: $response")
            } catch (e: Exception) {
                _history.value = WasteResponse(
                    success = false,
                    message = "Error fetching history: ${e.localizedMessage}"
                )
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun getDepartmentId(): Int? {
        return userPreference.departmentId.firstOrNull()
    }

    private suspend fun getUserToken(): String? {
        return userPreference.userToken.firstOrNull()
    }


}