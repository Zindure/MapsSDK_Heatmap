package com.example.mapssdk_heatmap

import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.example.mapssdk_heatmap.databinding.ActivityMapsBinding
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.android.gms.maps.model.*
import com.google.maps.android.data.geojson.GeoJsonLayer
import kotlinx.serialization.ExperimentalSerializationApi
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


    }




    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalSerializationApi::class)
    override fun onMapReady(googleMap: GoogleMap) {

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

        // List of JSON objects
        val displayedCountries = ArrayList<String>()
        // Loop through the countries and create associated layers
        val country = getBreedOrigins("Golden Retriever")
        val jsonString = country[0]
        displayedCountries.add(country[1])
        Log.d("GeoJSON : ", jsonString)
        val jsonObject = JSONObject(jsonString)
        val tmpLayer = GeoJsonLayer(mMap, jsonObject)
        val style = tmpLayer.defaultPolygonStyle
        style.fillColor = Color.BLUE
        style.strokeColor = Color.BLUE
        style.strokeWidth = 1f
        style.isClickable = false
        tmpLayer.addLayerToMap()


        //List of markers
        val markerList = mutableListOf<Marker>()

        //Listens for map Click Events
       /* mMap!!.setOnMapClickListener{

            //Here, clears the map
            mMap!!.clear()

            //Here, get country from click location
            val geocoder = Geocoder(this, Locale.getDefault())
            val addresses: List<Address> = geocoder.getFromLocation(it.latitude, it.longitude, 1) as List<Address>
            var country: String = ""
            if (addresses.isNotEmpty() && addresses[0].countryCode != null) {
                country = addresses[0].countryCode
            }

            // Marker Information String
            val displayedInformation : String = ""

            //Here, check if country is part of displayed countries
            if(displayedCountries.contains(country)){

                val clickedCountryLatLng = LatLng(it.latitude, it.longitude)
                val informationWindow = mMap?.addMarker(
                    MarkerOptions()
                        .position(clickedCountryLatLng)
                        .title(country.toString())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon))
                        .snippet(countries[displayedCountries.indexOf(country)][2])//Will contain breed repartition in
                )
                informationWindow?.alpha = 0.0f;
                informationWindow?.setInfoWindowAnchor(.5f, 1.0f);

                //Removes layer that has the same index as the country name from map and adds it again
                layerArray[displayedCountries.indexOf(country)].removeLayerFromMap()
                layerArray[displayedCountries.indexOf(country)].addLayerToMap()

                if (informationWindow != null) {
                    markerList.add(informationWindow)
                }
                informationWindow?.showInfoWindow()
            }

        }*/

    }


    private fun getBreedOrigins(breed: String): ArrayList<String> {


        //Reads from CSV File that contains country geoJSONS and names
        val file = applicationContext.assets.open("origin_data.csv")
            .bufferedReader()
            .use { it.readText()}
        var originCountry = ArrayList<String>()
        val rows: List<List<String>> = csvReader().readAll(file)
        for (row in rows){
            /*The first column contains the GeoJSON and the second the country name, both of
            which are functionally needed.
            All further rows, can be used to store additional data on the given country
            */
            if (row[1] == breed){
                originCountry.add(row[4])
                originCountry.add(row[3])
                originCountry.add(row[0])
            }
        }
        return originCountry



    }

    fun displayHeatMapBreed(breed: String){

    }
}