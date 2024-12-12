package com.dicoding.wiseorganic.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("data")
    val data: UserData? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class UserData(

    @field:SerializedName("departement_id")
    val departementId: Int? = null,

    @field:SerializedName("token")
    val token: String? = null,

    @field:SerializedName("username")
    val username: String? = null
)
