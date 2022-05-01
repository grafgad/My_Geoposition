package com.example.mygeoposition.view

import androidx.lifecycle.ViewModel
import com.example.mygeoposition.data.PlaceRepository
import com.example.mygeoposition.model.Place
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainViewModel : ViewModel() {

    var myLocation = LatLng(0.0, 0.0)
    private lateinit var myMarker: Place

    fun addMarkerToDB() {
        PlaceRepository.markersList.add(myMarker.copy())
    }

    fun findMe(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .position(myLocation)
                .title("This is my location")
                .snippet("first annotation")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
    }

    fun putMarker(googleMap: GoogleMap) {
        googleMap.addMarker(
            MarkerOptions()
                .title(myMarker.name)
                .position(myMarker.latLong)
                .snippet(myMarker.annotation)
        )
    }

    fun locateMarker(googleMap: GoogleMap) {
        val marker = Place("marker", LatLng(0.0, 0.0), "some annotation")
        googleMap.setOnMapClickListener {
            marker.latLong = LatLng(it.latitude, it.longitude)
        }
        myMarker = marker
    }
}