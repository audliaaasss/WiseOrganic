package com.dicoding.wiseorganic.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.wiseorganic.R
import com.dicoding.wiseorganic.databinding.ActivityMainBinding
import com.dicoding.wiseorganic.ui.data.NewDataActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.analysis,
            R.string.history
        )
    }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter

        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val addNewDataButton: Button = findViewById(R.id.addNewData)
        addNewDataButton.setOnClickListener {
            val intent = Intent(this, NewDataActivity::class.java)
            startActivity(intent)
        }

        supportActionBar?.elevation = 0f
    }
}