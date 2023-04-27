package ch.hslu.mobpro.mypokedex.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import ch.hslu.mobpro.firstpokedex.model.PokemonListAdapter
import ch.hslu.mobpro.mypokedex.databinding.FragmentLocationsBinding
import ch.hslu.mobpro.mypokedex.databinding.FragmentPokedexBinding
import ch.hslu.mobpro.mypokedex.model.LocationListAdapter
import ch.hslu.mobpro.mypokedex.model.PokeViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect


class LocationsFragment : Fragment() {

    private lateinit var adapter: LocationListAdapter

    private val pokeViewModel: PokeViewModel by viewModels()

    private var _binding: FragmentLocationsBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var dialogBuilder: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            pokeViewModel.requestLocationsList(1)
        }
    }

    //MAIN CODE
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter and set the layout manager outside the collect block
        adapter = LocationListAdapter(requireContext())
        binding.locationsList.adapter = adapter
        binding.locationsList.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // pass 1 to get locations from Kanto region
                pokeViewModel.locationFlow.collect { locations ->
                        // Update the adapter's data instead of creating a new adapter each time
                        adapter.updateData(locations)
                    }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}