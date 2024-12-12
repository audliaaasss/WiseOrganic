package com.dicoding.wiseorganic.data.auth

import com.dicoding.wiseorganic.data.request.LoginRequest
import com.dicoding.wiseorganic.data.request.RegisterRequest
import com.dicoding.wiseorganic.data.response.LoginResponse
import com.dicoding.wiseorganic.data.response.RegisterResponse
import com.dicoding.wiseorganic.data.retrofit.ApiService

class UserRepository(private val apiService: ApiService, private val dataStore: UserPreference? = null) {
    suspend fun registerUser(
        username: String,
        departmentId: Int,
        password: String
    ): RegisterResponse {
        // Buat objek RegisterRequest
        val request = RegisterRequest(
            username = username,
            departement_id = departmentId,
            password = password
        )

        // Panggil API dengan RegisterRequest
        return apiService.register(request)
    }

    suspend fun loginUser(
        username: String,
        password: String
    ): LoginResponse {
        // Periksa apakah dataStore telah diinisialisasi
        requireNotNull(dataStore) { "DataStore must be provided for login functionality." }

        val request = LoginRequest(username = username, password = password)
        val response = apiService.login(request)

        // Periksa apakah response.data tidak null
        val data = requireNotNull(response.data) { "Login response data is null." }

        // Save token and user info to DataStore
        dataStore.saveUserToken(data.token ?: throw IllegalArgumentException("Token is null"))
        dataStore.saveUsername(data.username ?: throw IllegalArgumentException("Username is null"))
        dataStore.saveDepartmentId(data.departementId ?: throw IllegalArgumentException("Department ID is null"))

        return response
    }
}