package com.example.mygeoposition.view

import androidx.recyclerview.widget.DiffUtil
import com.example.mygeoposition.model.Place

class MarkersDiffUtilCallback(
    private val newMarkersList: List<Place>,
    private val oldMarkersList : List<Place>
): DiffUtil.Callback() {

    override fun getOldListSize() = newMarkersList.size

    override fun getNewListSize() = oldMarkersList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMarker = oldMarkersList[oldItemPosition]
        val newMarker = newMarkersList[newItemPosition]
        return oldMarker.latLong == newMarker.latLong
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldMarker = oldMarkersList[oldItemPosition]
        val newMarker = newMarkersList[newItemPosition]
        return oldMarker == newMarker
    }
}