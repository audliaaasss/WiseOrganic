package com.dicoding.wiseorganic.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("departement_id")
    val departementId: String? = null,

    @field:SerializedName("username")
    val username: String? = null
)
