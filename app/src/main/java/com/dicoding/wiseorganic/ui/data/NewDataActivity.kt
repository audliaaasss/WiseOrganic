package com.dicoding.wiseorganic.ui.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.wiseorganic.R
import com.dicoding.wiseorganic.data.auth.UserPreference
import com.dicoding.wiseorganic.databinding.ActivityNewDataBinding
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class NewDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewDataBinding
    private val viewModel: DataViewModel by viewModels()
    private lateinit var userDataStore: UserPreference

    private var currentPhotoUri: Uri? = null

    private val categories = listOf("Wet Organic", "Pet", "Alumunium Can", "Tetra Pack", "Glass Bottle", "General Plastic Residue", "General Paper Residue", "Plastik Bag Liner", "Candles", "Slippers")

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userDataStore = UserPreference(this)

        setupCategoryDropdown()
        setupObservers()
        setupListeners()
    }

    private fun setupCategoryDropdown() {
        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            categories
        )
        binding.tvCategories.setAdapter(categoryAdapter)
    }

    private fun setupObservers() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressIndicator.visibility =
                if (isLoading) android.view.View.VISIBLE
                else android.view.View.GONE
        }

        viewModel.wasteResponse.observe(this) { response ->
            if (response.success == true) {
                Toast.makeText(this, "Waste data submitted successfully", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, response.message ?: "Failed to submit data", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.errorMessage.observe(this) { errorMessage ->
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupListeners() {
        binding.backButton.setOnClickListener { onBackPressedDispatcher.onBackPressed() }

        binding.cameraButton.setOnClickListener { checkCameraPermission() }

        binding.btnInput.setOnClickListener { submitWasteData() }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            openCamera()
        }
    }

    private fun openCamera() {
        // Use the utility method to get image URI
        currentPhotoUri = Utils.getImageUri(this)

        // Launch camera intent with the generated URI
        currentPhotoUri?.let { uri ->
            cameraResultLauncher.launch(uri)
        } ?: run {
            Toast.makeText(this, "Failed to create image file", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraResultLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            currentPhotoUri?.let { uri ->
                compressImage(uri)?.let { compressedUri ->
                    currentPhotoUri = compressedUri
                    binding.previewImageView.post {
                        Glide.with(this)
                            .load(compressedUri)
                            .error(R.drawable.gallery_icon) // Fallback image if loading fails
                            .into(binding.previewImageView)
                    }
                } ?: run {
                    Toast.makeText(this, "Failed to compress image", Toast.LENGTH_SHORT).show()
                    binding.previewImageView.setImageResource(R.drawable.gallery_icon)
                }
            } ?: run {
                Toast.makeText(this, "No image captured", Toast.LENGTH_SHORT).show()
                binding.previewImageView.setImageResource(R.drawable.gallery_icon)
            }
        } else {
            Toast.makeText(this, "Image capture was cancelled", Toast.LENGTH_SHORT).show()
            binding.previewImageView.setImageResource(R.drawable.gallery_icon)
        }
    }

    private fun compressImage(uri: Uri): Uri? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            val tempFile = File(cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
            val outputStream = FileOutputStream(tempFile)

            // Compress the image and save it to the temporary file
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
            outputStream.flush()
            outputStream.close()

            Uri.fromFile(tempFile)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun submitWasteData() {
        val category = binding.tvCategories.text.toString()
        val weightText = binding.edtUsername.text.toString()

        if (category.isEmpty() || weightText.isEmpty() || currentPhotoUri == null) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val categoryId = categories.indexOf(category) + 1
        val weight = weightText.toDoubleOrNull()

        if (weight == null) {
            Toast.makeText(this, "Invalid weight", Toast.LENGTH_SHORT).show()
            return
        }

        val file = uriToFile(currentPhotoUri!!, this)

        val maxFileSize = 5 * 1024 * 1024 // 5 MB dalam byte
        if (file.length() > maxFileSize) {
            Toast.makeText(this, "File size exceeds 5 MB", Toast.LENGTH_SHORT).show()
            return
        }

        val requestFile = file.asRequestBody("image/jpeg".toMediaType())
        val photoPart = MultipartBody.Part.createFormData("evidence_photo", file.name, requestFile)

        lifecycleScope.launch {
            userDataStore.userToken.collect { token ->
                if (!token.isNullOrEmpty()) {
                    val departmentId = 1

                    viewModel.submitWasteData(
                        token = "Bearer $token",
                        evidencePhoto = photoPart,
                        categoryId = categoryId,
                        weightKg = weight,
                        departmentId = departmentId
                    )
                } else {
                    Toast.makeText(this@NewDataActivity, "User not logged in", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val tempFile = File.createTempFile("temp_image", ".jpg", context.cacheDir)

        contentResolver.openInputStream(selectedImg)?.use { inputStream ->
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return tempFile
    }
}