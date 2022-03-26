package com.example.mygeoposition.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mygeoposition.databinding.ItemMarkerBinding
import com.example.mygeoposition.model.Place
import com.example.mygeoposition.view.MapsFragment.Companion.MARKERSLIST

class MarkerAdapter : RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>() {

     private var markersList: MutableList<Place> = mutableListOf()

     inner class MarkerViewHolder(private val binding: ItemMarkerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind (place: Place) {
            with(binding) {
                markerLatlong.text = place.latLong.toString()
                markerNameInput.setText(place.name)
                markerAnnotationInput.setText(place.annotation)

                markerDelete.setOnClickListener { deleteMarker(layoutPosition) }
                markerUpdate.setOnClickListener { updateMarker(layoutPosition) }
            }
        }

        private fun deleteMarker(layoutPosition: Int) {
            markersList.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

         private fun updateMarker(layoutPosition: Int) {
             markersList[layoutPosition] = Place(
                 binding.markerNameInput.text.toString(),
                 markersList[layoutPosition].latLong,
                 binding.markerAnnotationInput.text.toString()
             )
             notifyItemChanged(layoutPosition)
         }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MarkerViewHolder {
        val binding = ItemMarkerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MarkerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        holder.bind(markersList[position])
    }

    override fun getItemCount() = markersList.size

    fun setData() {
        markersList = MARKERSLIST
    }
}