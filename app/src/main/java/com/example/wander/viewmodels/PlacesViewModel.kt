package com.example.wander.viewmodels

import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class PlacesViewModel : ViewModel() {
    private val _places: MutableList<LatLng> = mutableListOf()
    val places: List<LatLng>
        get() = _places as List<LatLng>

    fun addPlace(place: LatLng) {
        _places.add(place)
    }
}