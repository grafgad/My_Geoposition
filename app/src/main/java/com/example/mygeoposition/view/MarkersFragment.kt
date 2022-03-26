package com.example.mygeoposition.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mygeoposition.databinding.FragmentMarkersBinding

class MarkersFragment: Fragment() {

    private var _binding: FragmentMarkersBinding? = null
    private val binding get() = _binding!!
    private val adapter = MarkerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMarkersBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.markersRecycler.adapter = adapter
        adapter.setData()
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance() = MarkersFragment()
    }
}