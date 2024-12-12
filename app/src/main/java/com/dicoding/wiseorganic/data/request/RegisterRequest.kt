package com.dicoding.wiseorganic.data.request

data class RegisterRequest(
    val username: String,
    val departement_id: Int,
    val password: String
)