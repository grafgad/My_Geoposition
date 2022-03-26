package com.example.mygeoposition.view

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.mygeoposition.R
import com.example.mygeoposition.data.PlaceRepository
import com.example.mygeoposition.databinding.FragmentMapsBinding
import com.example.mygeoposition.model.Place
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private var mapFragment: SupportMapFragment? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var myLocation : LatLng
    private lateinit var myMarker : Place

    private val findMe = OnMapReadyCallback { googleMap ->
        googleMap.addMarker(
            MarkerOptions()
                .position(myLocation)
                .title("This is my location")
                .snippet("first annotation")
        )
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
    }

    private val locateMarker = OnMapReadyCallback { it ->
        val marker = Place("marker", LatLng(0.0, 0.0), "some annotation")
        it.setOnMapClickListener {
            marker.latLong = LatLng(it.latitude, it.longitude)
        }
        myMarker = marker
    }

    private val putMarker = OnMapReadyCallback {
        it.addMarker(
            MarkerOptions()
                .title(myMarker.name)
                .position(myMarker.latLong)
                .snippet(myMarker.annotation)
        )
        MARKERSLIST.add(myMarker)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        checkLocationPermission()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(locateMarker)

        binding.whereIAm.setOnClickListener {
            mapFragment?.getMapAsync(findMe)
        }

        binding.putMarker.setOnClickListener {
            mapFragment?.getMapAsync(putMarker)
        }

        binding.markerList.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, MarkersFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkLocationPermission() {
        myLocation = LatLng(0.0,0.0)
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101
            )
            return
        }
        task.addOnSuccessListener {
            myLocation= LatLng(it.latitude, it.longitude)
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance() = MapsFragment()
        var MARKERSLIST = PlaceRepository().getMarkersList()
    }
}