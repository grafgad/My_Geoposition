package com.example.mygeoposition.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mygeoposition.R
import com.example.mygeoposition.databinding.FragmentMapsBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class MapsFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private var mapFragment: SupportMapFragment? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
    }

    override fun onStart() {
        super.onStart()
        checkLocationPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { viewModel.locateMarker(it) }
        binding.whereIAm.setOnClickListener {
            mapFragment?.getMapAsync { viewModel.findMe(it) }
        }
        binding.putMarker.setOnClickListener {
            viewModel.addMarkerToDB()
            mapFragment?.getMapAsync { viewModel.putMarker(it) }
        }
        binding.markerList.setOnClickListener {
            goToMarkersFragment()
        }
    }

    private fun goToMarkersFragment() {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, MarkersFragment.newInstance())
            .addToBackStack(null)
            .commit()
    }

    @SuppressLint("MissingPermission")
    private fun checkLocationPermission() {
        locationRequest()
        locationCallback()
        if (checkPermission()) {
            requestPermission()
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest(),
                locationCallback(),
                Looper.getMainLooper()
            )
        } else {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest(),
                locationCallback(),
                Looper.getMainLooper()
            )
        }
    }

    private fun locationRequest(): LocationRequest {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 5000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        return mLocationRequest
    }

    private fun locationCallback(): LocationCallback {
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    if (location != null) {
                        viewModel.myLocation = LatLng(location.latitude, location.longitude)
                    }
                }
            }
        }
        return mLocationCallback
    }

    private fun checkPermission(): Boolean {
        return (
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                )
    }

    private fun requestPermission() {
        return (ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 101
        ))
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance() = MapsFragment()
    }
}