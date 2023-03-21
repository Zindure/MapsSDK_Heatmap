package com.example.mapssdk_heatmap

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.mapssdk_heatmap.databinding.ActivityGeneralMapBinding

class GeneralMapActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityGeneralMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityGeneralMapBinding.inflate(layoutInflater)
     setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        binding.buttonFirst.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("breed", "French Bulldog")
            startActivity(intent)
        }
        binding.buttonFirst2.setOnClickListener {
            val intent = Intent(this, MapsHeatActivity::class.java)
            intent.putExtra("breed", "French Bulldog")
            startActivity(intent)
        }
    }
}