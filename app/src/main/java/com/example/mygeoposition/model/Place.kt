package com.example.mygeoposition.model

import com.google.android.gms.maps.model.LatLng

data class Place(
    var name: String?,
    var latLong: LatLng,
    var annotation: String?
)