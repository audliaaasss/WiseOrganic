package com.dicoding.wiseorganic.data.response

import com.google.gson.annotations.SerializedName

data class WasteResponse(

    @field:SerializedName("data")
    val data: WasteData? = null,

    @field:SerializedName("success")
    val success: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class Category(

    @field:SerializedName("category_description")
    val categoryDescription: String? = null,

    @field:SerializedName("category_name")
    val categoryName: String? = null
)

data class WasteData(

    @field:SerializedName("evidence_photo")
    val evidencePhoto: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("departement")
    val departement: Departement? = null,

    @field:SerializedName("category_id")
    val categoryId: Int? = null,

    @field:SerializedName("weight_kg")
    val weightKg: Double? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("category")
    val category: Category? = null,

    @field:SerializedName("departement_id")
    val departementId: Int? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null
)

data class Departement(

    @field:SerializedName("departement_description")
    val departementDescription: String? = null,

    @field:SerializedName("departement_name")
    val departementName: String? = null
)
