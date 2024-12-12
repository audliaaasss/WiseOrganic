package com.dicoding.wiseorganic.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.wiseorganic.R
import com.dicoding.wiseorganic.data.auth.UserRepository
import com.dicoding.wiseorganic.data.retrofit.ApiConfig
import com.dicoding.wiseorganic.databinding.ActivityRegisterBinding
import com.dicoding.wiseorganic.ui.factory.ViewModelFactory
import com.dicoding.wiseorganic.ui.login.LoginActivity


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup Department Dropdown
        val departments = listOf("Front Office", "Accounting", "HRD", "Security", "Restaurant and Bar", "Kitchen", "Spa", "Garden")
        val adapter = ArrayAdapter(this, R.layout.dropdown_item, departments)
        binding.tvDepartment.setAdapter(adapter)

        // Setup ViewModel
        val apiService = ApiConfig.getApiService()
        val repository = UserRepository(apiService)
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]

        // Setup register button click listener
        binding.btnRegister.setOnClickListener {
            registerUser()
        }

        // Observe register result
        viewModel.registerResult.observe(this) { result ->
            result.onSuccess { response ->
                Toast.makeText(this, "Registration Successful! User ID: ${response.id}", Toast.LENGTH_SHORT).show()
                finish()
            }.onFailure { exception ->
                Toast.makeText(this, "Registration Failed: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginTextView.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun registerUser() {
        val username = binding.edtUsername.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()
        val confirmPassword = binding.edtConfirmPassword.text.toString().trim()
        val department = binding.tvDepartment.text.toString().trim()

        // Validate inputs
        when {
            department.isEmpty() -> {
                binding.itDepartment.error = "Department is required"
                return
            }
            username.isEmpty() -> {
                binding.itUsername.error = "Username is required"
                return
            }
            password.isEmpty() -> {
                binding.itPassword.error = "Password is required"
                return
            }
            password.length < 8 -> {
                binding.itPassword.error = "Password must be at least 8 characters"
                return
            }
            confirmPassword.isEmpty() -> {
                binding.itConfirmPassword.error = "Confirm Password is required"
                return
            }
            password != confirmPassword -> {
                binding.itConfirmPassword.error = "Passwords do not match"
                return
            }
            else -> {
                // Clear previous errors
                binding.itDepartment.error = null
                binding.itUsername.error = null
                binding.itPassword.error = null
                binding.itConfirmPassword.error = null

                // Determine department ID (you might want to map this more robustly)
                val departmentId = when (department) {
                    "Front Office" -> 1
                    "Accounting" -> 2
                    "HRD" -> 3
                    "Kitchen" -> 4
                    "Spa" -> 5
                    "Garden" -> 6
                    "Security" -> 7
                    "Restaurant and Bar" -> 8
                    else -> 0
                }

                // Call ViewModel to register
                viewModel.registerUser(username, departmentId, password)
            }
        }
    }
}