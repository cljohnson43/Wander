package com.example.wander

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    /**
    override fun onStart() {
        super.onStart()

        val directions = PlacesFragmentDirections.actionPlacesFragmentToMapsFragment()
        findNavController(R.id.nav_host).navigate(directions)
    }
    */
}