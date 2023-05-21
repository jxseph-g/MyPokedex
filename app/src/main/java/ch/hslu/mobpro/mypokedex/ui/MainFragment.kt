package ch.hslu.mobpro.mypokedex.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import ch.hslu.mobpro.firstpokedex.model.PokemonListAdapter
import ch.hslu.mobpro.mypokedex.R
import ch.hslu.mobpro.mypokedex.databinding.FragmentMainBinding
import ch.hslu.mobpro.mypokedex.model.PokeViewModel
import ch.hslu.mobpro.mypokedex.model.PokemonDetailViewModel
import ch.hslu.mobpro.mypokedex.network.PokeApiService
import ch.hslu.mobpro.mypokedex.ui.PokedexFragment
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalTime
import kotlin.random.Random

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val pokeViewModel: PokeViewModel by viewModels()
    private val detailViewModel: PokemonDetailViewModel by activityViewModels()

    private var pokemonListForSearch: List<PokeApiService.Pokemon>? = emptyList()
    private lateinit var adapter: PokemonListAdapter


    //On App start, create a random number to display a random pokemon picture on the home screen
    var randomNr = Random.nextInt(1, 152)

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var trainerName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Initialize shared preferences
        sharedPreferences =
            requireActivity().getSharedPreferences("pokedex_preferencesTEST", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        trainerName = sharedPreferences.getString("trainer_name", null)

    }

    override fun onResume() {
        super.onResume()

        if (isNetworkAvailable()) {
            binding.noInternetText.visibility = View.GONE
        } else {
            binding.noInternetText.visibility = View.VISIBLE
            binding.buttonsGrid.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        // Initialize the adapter
        binding.buttonsGrid.visibility = View.VISIBLE
        binding.appBarLayout.visibility = View.VISIBLE
        binding.recyclerViewSearch.visibility = View.GONE

        adapter = PokemonListAdapter(emptyList())

        // Set the adapter to the RecyclerView
        binding.recyclerViewSearch.adapter = adapter
        binding.recyclerViewSearch.layoutManager = LinearLayoutManager(requireContext())

        pokeViewModel.requestPokeList()
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                addPokemonToSearchList()
            }
        }
        return binding.root
    }

    /*
    --> MAIN CODE
     */
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentTimeText = getDayTimeText()
        binding.trainerName.text = currentTimeText

        //Load pokemon nr with randomNr with Picasso
        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$randomNr.png")
            .into(binding.pokemonHeaderImage)

        //Hide buttons when text is entered in the searchView
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    filterPokemon(newText.orEmpty())
                    binding.buttonsGrid.visibility = View.GONE
                    binding.appBarLayout.visibility = View.GONE
                    binding.recyclerViewSearch.visibility = View.VISIBLE

                    // Set the click listener for the adapter
                    adapter.setOnItemClickListener(object : PokemonListAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val selectedPokemon = adapter.getItem(position)
                            if (selectedPokemon != null) {
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
                        }
                    })
                } else {
                    binding.buttonsGrid.visibility = View.VISIBLE
                    binding.appBarLayout.visibility = View.VISIBLE
                    binding.recyclerViewSearch.visibility = View.GONE
                }
                return true
            }
        })

        // "Pokedex" Button - opens the full pokedex view --> PokedexFragment.kt
        binding.pokedexButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            transaction.replace(R.id.fragment_container, PokedexFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        //"All Locations" Button - opens all the locations --> CityFragment
        binding.locationsButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            transaction.replace(R.id.fragment_container, LocationsFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        //"Gym Badges" to display the 6 Gym Badges out of Gen 1
        binding.badgesButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            transaction.replace(R.id.fragment_container, GymBadgesFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        //"Trainer Card with favorites"
        binding.trainerCardButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            transaction.replace(R.id.fragment_container, TrainerCardFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }

        //"Type Charts Button"
        binding.typeChartsButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            transaction.replace(R.id.fragment_container, TypeChartFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    private suspend fun addPokemonToSearchList() {
        pokeViewModel.pokeFlow.collect { pokemonList ->
            pokemonListForSearch = pokemonList
            adapter.updateData(pokemonList)
        }
    }

    private fun filterPokemon(searchQuery: String) {
        val filteredList = if (searchQuery.isBlank()) {
            pokemonListForSearch
        } else {
            pokemonListForSearch?.filter { pokemon ->
                pokemon.name?.contains(searchQuery, ignoreCase = true) == true
            }
        }
        adapter.updateData(filteredList ?: emptyList())
    }

    fun getCurrentTime(): LocalTime {
        return LocalTime.now()
    }

    fun getDayTimeText(): String {
        val currentTime = getCurrentTime()
        val startMorningTime = LocalTime.of(4, 59)
        val startAfternoonTime = LocalTime.of(12,59)
        val startEveningTime = LocalTime.of(17,59)

        if (currentTime.isAfter(startMorningTime) && currentTime.isBefore(startAfternoonTime)) {
            return "Good Morning!"
        } else if (currentTime.isAfter(startAfternoonTime) && currentTime.isBefore(startEveningTime)) {
            return "Good Afternoon!"
        } else if (currentTime.isAfter(startEveningTime) || currentTime.isBefore(startMorningTime)) {
            return "Good Evening!"
        } else {
            return "Hello, Trainer!"
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}