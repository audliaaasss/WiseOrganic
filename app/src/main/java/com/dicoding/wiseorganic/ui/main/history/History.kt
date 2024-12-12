package com.dicoding.wiseorganic.ui.main.history

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class History (
    val departementName: String,
    val created: String,
    val updated: String
) : Parcelable