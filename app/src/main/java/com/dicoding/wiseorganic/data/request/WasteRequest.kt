package com.dicoding.wiseorganic.data.request

data class WasteRequest(
    val evidence_photo: String?,
    val category_id: Int,
    val weight_kg: Number,
    val departement_id: Int
)