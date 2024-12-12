package com.dicoding.wiseorganic.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.dicoding.wiseorganic.data.auth.UserPreference
import com.dicoding.wiseorganic.data.auth.UserRepository
import com.dicoding.wiseorganic.data.retrofit.ApiConfig
import com.dicoding.wiseorganic.databinding.ActivityLoginBinding
import com.dicoding.wiseorganic.ui.factory.ViewModelFactory
import com.dicoding.wiseorganic.ui.main.MainActivity
import com.dicoding.wiseorganic.ui.register.RegisterActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var userDataStore: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize UserDataStore
        userDataStore = UserPreference(this)

        // Setup ViewModel
        val apiService = ApiConfig.getApiService()
        val repository = UserRepository(apiService, userDataStore)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        // Setup login button
        binding.btnLogin.setOnClickListener {
            performLogin()
        }

        // Observe login result
        viewModel.loginResult.observe(this) { result ->
            result.onSuccess { loginResponse ->
                val token = loginResponse.data?.token
                val departmentId = loginResponse.data?.departementId

                if (!token.isNullOrEmpty()) {
                    lifecycleScope.launch {
                        userDataStore.saveUserToken(token)

                        if (departmentId != null) {
                            userDataStore.saveDepartmentId(departmentId)
                        }
                    }
                }
                // Navigate to main activity or dashboard
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }.onFailure { exception ->
                // Show error message
                Toast.makeText(this, "Login Failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        // Optional: Register text click listener
        binding.registerTextView.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        val username = binding.edtUsername.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        when {
            username.isEmpty() -> {
                binding.itUsername.error = "Username is required"
                return
            }
            password.isEmpty() -> {
                binding.itPassword.error = "Password is required"
                return
            }
            else -> {
                // Clear previous errors
                binding.itUsername.error = null
                binding.itPassword.error = null

                // Attempt login
                viewModel.login(username, password)
            }
        }
    }
}