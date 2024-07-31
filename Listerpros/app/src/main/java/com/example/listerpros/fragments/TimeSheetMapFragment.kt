package com.example.listerpros.fragments

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.example.listerpros.databinding.FragmentTimeSheetMapBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class TimeSheetMapFragment : Fragment(),OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private lateinit var googleMap: GoogleMap
    private lateinit var binding: FragmentTimeSheetMapBinding
    private var currentLocation: Location?=null
    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val requestCode = 101
    var latitude: Double = 23.65
    var longitude:Double = 23.65


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding= FragmentTimeSheetMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(requireContext())
        fetchLocation()
        binding.mapView.onCreate(savedInstanceState)

    }

    // To fetch the Current Location
    private fun fetchLocation() {
       if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )

        {
            ActivityCompat.requestPermissions(requireContext() as Activity, arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION),requestCode)
            return
        }
        val task=fusedLocationProviderClient!!.lastLocation
        task.addOnSuccessListener { location->
            if(location!=null)
            {
                currentLocation=location
                latitude=currentLocation!!.latitude
                longitude=currentLocation!!.longitude

                binding.mapView.onResume()
                binding.mapView.getMapAsync(this)
            }

        }
    }

   // Map display with current Location
    override fun onMapReady(map: GoogleMap) {
        googleMap= map
        googleMap.setOnMarkerClickListener(this)
        map.uiSettings.isZoomControlsEnabled=true
        val latlng= LatLng(currentLocation!!.latitude,currentLocation!!.longitude)
        val markerOptions= MarkerOptions().position(latlng).title("current location")
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latlng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,15f))
        googleMap.addMarker(markerOptions)

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode)
        {
            requestCode ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    fetchLocation()
                }
            }
        }
    }

    override fun onMarkerClick(p0: Marker): Boolean =false
}