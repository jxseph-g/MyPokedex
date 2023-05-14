package ch.hslu.mobpro.mypokedex.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import ch.hslu.mobpro.firstpokedex.model.PokemonListAdapter
import ch.hslu.mobpro.mypokedex.R
import ch.hslu.mobpro.mypokedex.databinding.FragmentPokedexBinding
import ch.hslu.mobpro.mypokedex.model.PokeViewModel
import ch.hslu.mobpro.mypokedex.model.PokemonDetailViewModel
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

    private val pokeViewModel: PokeViewModel by viewModels()

    private val detailViewModel: PokemonDetailViewModel by activityViewModels()

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
        pokeViewModel.requestPokeList()
    }

    //MAIN CODE
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PokemonListAdapter(emptyList())
        binding.pokemonList.adapter = adapter
        binding.pokemonList.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                pokeViewModel.pokeFlow.collect { pokemons ->
                    // Update the adapter's data instead of creating a new adapter each time
                    adapter.updateData(pokemons)

                    // Set an onItemClickListener for the adapter
                    adapter.setOnItemClickListener(object : PokemonListAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            // Handle item click
                            val selectedPokemon = pokemons[position]
                            detailViewModel.setSelectedPokemon(selectedPokemon)

                            // Navigate to PokemonDetailFragment
                            val transaction = parentFragmentManager.beginTransaction()
                            transaction.setCustomAnimations(
                                R.anim.slide_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.slide_out
                            )
                            transaction.replace(R.id.fragment_container, PokemonDetailFragment())
                            transaction.addToBackStack(null)
                            transaction.commit()
                        }
                    })
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}