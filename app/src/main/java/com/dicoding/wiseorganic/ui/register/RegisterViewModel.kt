package com.dicoding.wiseorganic.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.wiseorganic.data.auth.UserRepository
import com.dicoding.wiseorganic.data.response.RegisterResponse
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel(private val repository: UserRepository) : ViewModel() {
    private val _registerResult = MutableLiveData<Result<RegisterResponse>>()
    val registerResult: LiveData<Result<RegisterResponse>> = _registerResult

    fun registerUser(
        username: String, departmentId: Int, password: String
    ) {
        viewModelScope.launch {
            try {
                val response = repository.registerUser(username, departmentId, password)
                _registerResult.value = Result.success(response)
            } catch (e: HttpException) {
                _registerResult.value = Result.failure(e)
            } catch (e: Exception) {
                _registerResult.value = Result.failure(e)
            }
        }
    }
}