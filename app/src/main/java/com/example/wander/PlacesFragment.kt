package com.example.wander

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wander.viewmodels.PlacesViewModel

class PlacesFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_places, container, false)
    }
}