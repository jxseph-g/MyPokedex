package ch.hslu.mobpro.mypokedex.ui

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import ch.hslu.mobpro.mypokedex.R
import ch.hslu.mobpro.mypokedex.databinding.FragmentPokemonDetailBinding
import ch.hslu.mobpro.mypokedex.model.PokeViewModel
import ch.hslu.mobpro.mypokedex.model.PokemonDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.util.*

/*
PokemonDetailFragment -> This class should load all the relevant information from
the API regarding the Pokemon Clicked in the PokedexView or searched by
 */
class PokemonDetailFragment : Fragment() {

    private val viewModel: PokemonDetailViewModel by activityViewModels()
    private val pokeViewModel: PokeViewModel by viewModels()


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

            // Extract Pokemon ID
            val pokemonId = pokemon.id ?: return@Observer

            // Request Pokemon details = details such as types and also species / description text
            lifecycleScope.launch {
                val fullPokemon = pokeViewModel.getPokemonDetails(pokemonId)
                val pokemonSpecies = pokeViewModel.getPokemonSpecies(pokemonId)

                val types = fullPokemon?.types?.map { it.type.name }

                //now extract types from json
                if (types != null && types.size < 2) {
                    val type1 = types?.getOrNull(0)
                    val type1Color = getBackgroundColor(type1)
                    val type1ColorConv = getResources().getColor(type1Color)
                    binding.pokemonType2.text = type1
                    binding.pokemonType2.setBackgroundColor(type1ColorConv)
                    //set type1 with no text to push type2 to the bottom
                    binding.pokemonType1.text = ""
                } else {
                    val type1 = types?.getOrNull(0)
                    val type1Color = getBackgroundColor(type1)
                    val type1ColorConv = getResources().getColor(type1Color)
                    //val type1 = "fire"
                    val type2 = types?.getOrNull(1)
                    val type2Color = getBackgroundColor(type2)
                    val type2ColorConv = getResources().getColor(type2Color)
                    //val type2 = "fire"
                    binding.pokemonType1.text = type1
                    binding.pokemonType1.setBackgroundColor(type1ColorConv)
                    binding.pokemonType2.text = type2
                    binding.pokemonType2.setBackgroundColor(type2ColorConv)
                }

                //stats of the pokemon
                val hp = fullPokemon?.stats?.firstOrNull { it.stat.name == "hp" }?.baseStat ?: 0
                binding.hpValue.text = hp.toString()

                val attack = fullPokemon?.stats?.firstOrNull { it.stat.name == "attack" }?.baseStat ?: 0
                binding.attValue.text = attack.toString()

                val defense = fullPokemon?.stats?.firstOrNull { it.stat.name == "defense" }?.baseStat ?: 0
                binding.defValue.text = defense.toString()

                val specialAttack = fullPokemon?.stats?.firstOrNull { it.stat.name == "special-attack" }?.baseStat ?: 0
                binding.spzValue.text = specialAttack.toString()

                val speed = fullPokemon?.stats?.firstOrNull { it.stat.name == "speed" }?.baseStat ?: 0
                binding.spdValue.text = speed.toString()

                //get pokemon text description in english
                val description = pokemonSpecies?.flavorTextEntries?.firstOrNull { it.language.name == "en" }?.flavorText

                //replace line breaks from api with space
                val formattedDescription = description?.replace("\n", " ")?.trim()

                //bind beauuuutiful description to layout =)
                binding.pokemonDescription.text = formattedDescription
            }

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

fun getBackgroundColor(type: String?): Int {
    if (type != null) {
        return when (type.lowercase(Locale.getDefault())) {
            "normal" -> R.color.normal_type
            "fire" -> R.color.fire_type
            "water" -> R.color.water_type
            "electric" -> R.color.electric_type
            "grass" -> R.color.grass_type
            "ice" -> R.color.ice_type
            "fighting" -> R.color.fighting_type
            "poison" -> R.color.poison_type
            "ground" -> R.color.ground_type
            "flying" -> R.color.flying_type
            "psychic" -> R.color.psychic_type
            "bug" -> R.color.bug_type
            "rock" -> R.color.rock_type
            "ghost" -> R.color.ghost_type
            "dragon" -> R.color.dragon_type
            "dark" -> R.color.dark_type
            "steel" -> R.color.steel_type
            "fairy" -> R.color.fairy_type
            else -> R.color.semi_transp
        }
    }
    return R.color.black
}


