package com.dicoding.wiseorganic.data.retrofit

import com.dicoding.wiseorganic.data.request.LoginRequest
import com.dicoding.wiseorganic.data.request.RegisterRequest
import com.dicoding.wiseorganic.data.response.LoginResponse
import com.dicoding.wiseorganic.data.response.RegisterResponse
import com.dicoding.wiseorganic.data.response.WasteResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @Multipart
    @POST("waste-records")
    suspend fun postWaste(
        @Header("Authorization") token: String,
        @Part evidencePhoto: MultipartBody.Part,
        @Part("category_id") categoryId: Int,
        @Part("weight_kg") weightKg: Number,
        @Part("departement_id") departmentId: Int,
    ): WasteResponse

    @GET("waste-records/{id}")
    suspend fun history(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): WasteResponse
}