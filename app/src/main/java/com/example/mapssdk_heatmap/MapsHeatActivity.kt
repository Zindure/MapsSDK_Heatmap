package com.example.mapssdk_heatmap

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.annotation.RequiresApi

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.android.gms.maps.model.*
import com.google.maps.android.data.geojson.GeoJsonLayer
import kotlinx.serialization.ExperimentalSerializationApi
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MapsHeatActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.heat_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.heat) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSerializationApi::class)
    override fun onMapReady(googleMap: GoogleMap) {

        val previousButton = findViewById<Button>(R.id.previousButton)

        previousButton.setOnClickListener {
            val intent = Intent(this, GeneralMapActivity::class.java)
            startActivity(intent)
        }

        mMap = googleMap
        mMap!!.uiSettings.isZoomControlsEnabled = true

        //Map Style
        val success = googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                this, R.raw.style_json
            )
        )

        //Sets default location of map
        val defaultFocus = LatLng(40.416775, -3.70379)
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultFocus, 3f))

        // Loop through the countries and create associated layers
        val breed = intent.getStringExtra("breed") ?: "Golden Retriever"
        val popularCountries = getBreedPopularity(breed)
        val jsonString = popularCountries
        Log.d("GeoJSON : ", jsonString)
        val jsonObject = JSONObject(jsonString)
        val tmpLayer = GeoJsonLayer(mMap, jsonObject)
        val style = tmpLayer.defaultPolygonStyle
        style.fillColor = Color.BLUE
        style.strokeColor = Color.BLUE
        style.strokeWidth = 1f
        style.isClickable = false
        tmpLayer.addLayerToMap()


    }


    private fun getBreedPopularity(breed: String): String {


        //Reads from CSV File that contains country geoJSONS and names
        val file = applicationContext.assets.open("heat_data.csv")
            .bufferedReader()
            .use { it.readText()}
        var popularity = ""
        val rows: List<List<String>> = csvReader().readAll(file)
        for (row in rows){
            /*The first column contains the GeoJSON and the second the country name, both of
            which are functionally needed.
            All further rows, can be used to store additional data on the given country
            */
            if (row[1] == breed){
                println(row[0])
                println(row[1])
                println(row[2])
                popularity = row[2]
            }
        }
        return popularity



    }

    fun displayHeatMapBreed(breed: String){

    }
}