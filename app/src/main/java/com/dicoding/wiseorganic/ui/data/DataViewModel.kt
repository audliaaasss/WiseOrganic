package com.dicoding.wiseorganic.ui.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.wiseorganic.data.request.WasteRequest
import com.dicoding.wiseorganic.data.response.WasteResponse
import com.dicoding.wiseorganic.data.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class DataViewModel : ViewModel() {
    private val _wasteResponse = MutableLiveData<WasteResponse>()
    val wasteResponse: LiveData<WasteResponse> = _wasteResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun submitWasteData(token: String,
                        evidencePhoto: MultipartBody.Part,
                        categoryId: Int,
                        weightKg: Number,
                        departmentId: Int
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val apiService = ApiConfig.getApiService()
                val response = apiService.postWaste(token, evidencePhoto, categoryId, weightKg, departmentId)
                _wasteResponse.value = response
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An unknown error occurred"
                _isLoading.value = false
            }
        }
    }
}