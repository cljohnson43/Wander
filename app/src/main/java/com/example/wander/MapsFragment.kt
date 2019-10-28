package com.example.wander

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wander.viewmodels.PlacesViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.util.*

class MapsFragment : Fragment(), OnMapReadyCallback {

    private val TAG = MapsFragment::class.java.simpleName
    private lateinit var map: GoogleMap
    private val REQUEST_FINE_LOCATION_CODE = 0

    private val viewModel: PlacesViewModel by lazy {
        ViewModelProvider(requireActivity(), ViewModelProvider.NewInstanceFactory()).get(
            PlacesViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps, container, false)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return view
    }

    override fun onStart() {
        super.onStart()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        val sydney = LatLng(-34.0, 151.0)
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        val groundOverlay =
            GroundOverlayOptions().image(BitmapDescriptorFactory.fromResource(R.drawable.android))
                .position(sydney, 100f)
        map.addGroundOverlay(groundOverlay)

        setMapLongClick(map)
        setPoiClick(map)
        setMapStyle(map)
        enableMyLocation()
        displayMarkers(map)
    }

    /**
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    requireActivity().menuInflater.inflate(R.menu.map_options, menu)
    return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
    R.id.normal_map -> map.mapType = GoogleMap.MAP_TYPE_NORMAL
    R.id.terrain_map -> map.mapType = GoogleMap.MAP_TYPE_TERRAIN
    R.id.satelite_map -> map.mapType = GoogleMap.MAP_TYPE_SATELLITE
    R.id.hybrid_map -> map.mapType = GoogleMap.MAP_TYPE_HYBRID
    }
    return true
    }

     */
    fun setMapLongClick(gMap: GoogleMap) {
        gMap.setOnMapLongClickListener { latLng ->
            val snippet = String.format(
                Locale.getDefault(),
                "Lat: %1$.5f, Long: %2$.5f",
                latLng.latitude,
                latLng.longitude
            )
            gMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(getString(R.string.dropped_pin))
                    .snippet(snippet)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            )

            viewModel.addPlace(latLng)
        }
    }

    fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            map.addMarker(
                MarkerOptions()
                    .position(poi.latLng)
                    .title(poi.name)
            ).let {
                it.showInfoWindow()
            }
        }
    }

    fun setMapStyle(map: GoogleMap) {
        try {
            val success =
                map.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                        requireActivity(),
                        R.raw.map_style
                    )
                )

            if (!success) {
                Log.e(TAG, "Style parsing failed")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "can't find style file: Error: ", e)
        }
    }

    fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun enableMyLocation() {
        if (isPermissionGranted()) {
            map.isMyLocationEnabled = true

        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_FINE_LOCATION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_FINE_LOCATION_CODE &&
            grantResults.size > 0 &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            enableMyLocation()
        }
    }

    fun displayMarkers(map: GoogleMap) {
        val places = viewModel.places

        if (places.size > 0) {
            places.forEach { place ->
                map.addMarker(
                    MarkerOptions()
                        .position(place)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                )
            }
        }
    }
}
