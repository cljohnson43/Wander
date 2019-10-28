package com.example.wander

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.wander.viewmodels.PlacesViewModel

class MainActivity : AppCompatActivity() {
    private val viewModel: PlacesViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(PlacesViewModel::class.java)
    }

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