package ch.hslu.mobpro.mypokedex.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import ch.hslu.mobpro.mypokedex.R
import ch.hslu.mobpro.mypokedex.databinding.FragmentPokemonDetailBinding
import ch.hslu.mobpro.mypokedex.model.PokeViewModel
import ch.hslu.mobpro.mypokedex.model.PokemonDetailViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/*
PokemonDetailFragment -> This class should load all the relevant information from
the API regarding the Pokemon Clicked in the PokedexView or searched by
 */
class PokemonDetailFragment : Fragment() {

    private val viewModel: PokemonDetailViewModel by activityViewModels()
    private val pokeViewModel: PokeViewModel by viewModels()

    private var clicked: Int = 0

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var favoriteList: MutableList<String> = mutableListOf()

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Initialize shared preferences
        sharedPreferences = requireActivity().getSharedPreferences("pokedex_preferencesTEST", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        favoriteList =
            sharedPreferences.getStringSet("favoriteList", setOf<String>())?.toMutableList() ?: mutableListOf()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getSelectedPokemon().observe(viewLifecycleOwner, Observer { pokemon ->

            // Extract Pokemon ID
            val pokemonId = pokemon.id ?: return@Observer
            val pokemonIdString = pokemonId.toString()

            //check if Pokemon is already in the favorites
            var isFavorite = favoriteList.contains(pokemonIdString)

            if (isFavorite){
                binding.setFavoriteFloatingButton.setImageResource(R.drawable.baseline_star_24)
            } else if (!isFavorite) {
                binding.setFavoriteFloatingButton.setImageResource(R.drawable.baseline_star_border_24)
            }

                // Request Pokemon details = details such as types and also species / description text
            lifecycleScope.launch {
                val fullPokemon = pokeViewModel.getPokemonDetails(pokemonId)
                val pokemonSpecies = pokeViewModel.getPokemonSpecies(pokemonId)

                val types = fullPokemon?.types?.map { it.type.name }

                //now extract types from json
                if (types != null && types.size < 2) {
                    val type1 = types.getOrNull(0)
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
                //hp
                val hp = fullPokemon?.stats?.firstOrNull { it.stat.name == "hp" }?.baseStat ?: 0
                binding.hpValue.text = hp.toString()
                val hpProgressBar = binding.hpProgressBar
                hpProgressBar.progress = hp

                //attack
                val attack = fullPokemon?.stats?.firstOrNull { it.stat.name == "attack" }?.baseStat ?: 0
                binding.attValue.text = attack.toString()
                val attackProgressBar = binding.attackProgressBar
                attackProgressBar.progress = attack

                //def
                val defense = fullPokemon?.stats?.firstOrNull { it.stat.name == "defense" }?.baseStat ?: 0
                binding.defValue.text = defense.toString()
                val defenseProgressBar = binding.defenseProgressBar
                defenseProgressBar.progress = defense

                //specialAttack or spz
                val specialAttack = fullPokemon?.stats?.firstOrNull { it.stat.name == "special-attack" }?.baseStat ?: 0
                binding.spzValue.text = specialAttack.toString()
                val specialAttackeProgressBar = binding.spzProgressBar
                specialAttackeProgressBar.progress = specialAttack

                //speed or spd
                val speed = fullPokemon?.stats?.firstOrNull { it.stat.name == "speed" }?.baseStat ?: 0
                binding.spdValue.text = speed.toString()
                val speedProgressBar = binding.spdProgressBar
                speedProgressBar.progress = speed

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

            binding.pokemonImage.setOnClickListener {
                clicked++
                if(clicked == 4) {
                    val toastMessage = "something is happening..."
                    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                    editor.putStringSet("favoriteList", favoriteList.toSet()).apply()
                }
                if(clicked == 9) {
                    val toastMessage = "a few more..."
                    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                    editor.putStringSet("favoriteList", favoriteList.toSet()).apply()
                }

                if(clicked == 12) {
                    val toastMessage = "unlocked shiny :)"
                    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                    editor.putStringSet("favoriteList", favoriteList.toSet()).apply()
                    Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/shiny/${pokemon.id}.png")
                        .into(binding.pokemonImage)
                }
            }

            binding.setFavoriteFloatingButton.setOnClickListener {
                if(!isFavorite){
                    favoriteList.add(pokemonIdString)
                    isFavorite = true
                    binding.setFavoriteFloatingButton.setImageResource(R.drawable.baseline_star_24)
                    val toastMessage = "Pokémon added to favorites"
                    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                    editor.putStringSet("favoriteList", favoriteList.toSet()).apply()
                } else if (isFavorite) {
                    favoriteList.remove(pokemonIdString)
                    isFavorite = false
                    binding.setFavoriteFloatingButton.setImageResource(R.drawable.baseline_star_border_24)
                    val toastMessage = "Pokémon removed from favorites"
                    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
                    editor.putStringSet("favoriteList", favoriteList.toSet()).apply()
                }
            }
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


