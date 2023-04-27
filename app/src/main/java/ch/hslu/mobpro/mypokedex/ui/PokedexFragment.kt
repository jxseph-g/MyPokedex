package ch.hslu.mobpro.mypokedex.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import ch.hslu.mobpro.firstpokedex.model.PokemonListAdapter
import ch.hslu.mobpro.mypokedex.databinding.FragmentPokedexBinding
import ch.hslu.mobpro.mypokedex.model.PokeViewModel
import kotlinx.coroutines.launch

/*
This Class is responsible for displaying the FULL POKEDEX
It displays a list of Pokémon fetched from the PokeMainViewModel.
The class inflates its layout using the FragmentPokedexBinding class, sets up a
RecyclerView with a PokemonListAdapter, and populates the list with the Pokémon data
collected from the PokeViewModel's pokeFlow.
 */
class PokedexFragment : Fragment() {

    private lateinit var adapter: PokemonListAdapter

    private val pokeMainViewModel: PokeViewModel by viewModels()

    private var _binding: FragmentPokedexBinding? = null

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
        _binding = FragmentPokedexBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        pokeMainViewModel.requestPokeList()
    }

    //MAIN CODE
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {

            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                pokeMainViewModel.pokeFlow.collect { pokemons ->

                    val adapter = PokemonListAdapter(pokemons)

                    binding.pokemonList.adapter = adapter

                    binding.pokemonList.layoutManager = LinearLayoutManager(requireContext())

                    //just to add a text to see how many pkmn it loads
                    //binding.pokemonNr.text = "#" + if (pokemons.isEmpty()) 0 else pokemons.size

                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}