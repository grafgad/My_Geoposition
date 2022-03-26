package com.example.mygeoposition.data

import com.example.mygeoposition.model.Place

class PlaceRepository {
    private var markersList = mutableListOf<Place>()

    @JvmName("getMarkersList1")
    fun getMarkersList() = markersList
}