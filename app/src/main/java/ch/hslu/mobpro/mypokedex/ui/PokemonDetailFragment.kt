package ch.hslu.mobpro.mypokedex.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import ch.hslu.mobpro.mypokedex.databinding.FragmentPokemonDetailBinding
import ch.hslu.mobpro.mypokedex.model.PokemonDetailViewModel
import com.squareup.picasso.Picasso

/*
PokemonDetailFragment -> This class should load all the relevant information from
the API regarding the Pokemon Clicked in the PokedexView or searched by
 */
class PokemonDetailFragment : Fragment() {

    private val viewModel: PokemonDetailViewModel by activityViewModels()

    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSelectedPokemon().observe(viewLifecycleOwner, Observer { pokemon ->

            //capitalize pokemon names
            val name = pokemon.name?.capitalize()

            // Format the Pokedex ID with leading zeros
            val pokedexId = pokemon.id?.toString()?.padStart(3, '0')

            // Update the UI with the selected Pokemon data
            binding.pokemonName.text = name
            binding.pokemonNumber.text = pokedexId
            Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.id}.png")
                .into(binding.pokemonImage)

        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
