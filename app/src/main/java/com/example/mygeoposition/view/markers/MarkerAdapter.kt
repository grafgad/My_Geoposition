package com.example.mygeoposition.view.markers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mygeoposition.data.PlaceRepository
import com.example.mygeoposition.databinding.ItemMarkerBinding
import com.example.mygeoposition.model.Place

class MarkerAdapter : RecyclerView.Adapter<MarkerAdapter.MarkerViewHolder>() {

    private var markersList: MutableList<Place> = mutableListOf()

    class MarkerViewHolder(
        private val binding: ItemMarkerBinding,
        private val deleteMarker: (Int) -> Unit,
        private val updateMarker: (Int, String, String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(place: Place) {
            with(binding) {
                markerLatlong.text = place.latLong.toString()
                markerNameInput.setText(place.name)
                markerAnnotationInput.setText(place.annotation)

                markerDelete.setOnClickListener { deleteMarker(layoutPosition) }
                markerUpdate.setOnClickListener {
                    updateMarker(
                        layoutPosition,
                        markerNameInput.text.toString(),
                        markerAnnotationInput.text.toString()
                    )
                }
            }
        }
    }

    private fun deleteMarker(layoutPosition: Int) {
        markersList.removeAt(layoutPosition)
        notifyItemRemoved(layoutPosition)
    }

    private fun updateMarker(
        layoutPosition: Int,
        markerNameInput: String,
        markerAnnotationInput: String
    ) {
        markersList[layoutPosition] = Place(
            markerNameInput,
            markersList[layoutPosition].latLong,
            markerAnnotationInput
        )
        notifyItemChanged(layoutPosition)
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
        return MarkerViewHolder(binding, ::deleteMarker, ::updateMarker)
    }

    override fun onBindViewHolder(holder: MarkerViewHolder, position: Int) {
        holder.bind(markersList[position])
    }

    override fun getItemCount() = markersList.size

    fun setData(newMarkers: List<Place>) {
        val callback = MarkersDiffUtilCallback(newMarkers, markersList)
        val diffResult = DiffUtil.calculateDiff(callback)
        markersList = PlaceRepository.markersList
        diffResult.dispatchUpdatesTo(this)
    }
}