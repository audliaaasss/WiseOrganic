package com.dicoding.wiseorganic.data.auth

data class UserModel(
    val username: String,
    val token: String,
    val isLogin: Boolean = false
)