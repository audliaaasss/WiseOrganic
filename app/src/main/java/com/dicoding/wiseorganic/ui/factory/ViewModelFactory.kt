package com.dicoding.wiseorganic.ui.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.wiseorganic.data.auth.UserRepository
import com.dicoding.wiseorganic.ui.login.LoginViewModel
import com.dicoding.wiseorganic.ui.main.history.History
import com.dicoding.wiseorganic.ui.main.history.HistoryViewModel
import com.dicoding.wiseorganic.ui.register.RegisterViewModel

class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) ->
                RegisterViewModel(repository) as T
            modelClass.isAssignableFrom(LoginViewModel::class.java) ->
                LoginViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}