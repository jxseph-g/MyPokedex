package ch.hslu.mobpro.mypokedex.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import ch.hslu.mobpro.firstpokedex.model.PokemonListAdapter
import ch.hslu.mobpro.mypokedex.R
import ch.hslu.mobpro.mypokedex.databinding.FragmentTrainerCardBinding
import ch.hslu.mobpro.mypokedex.model.PokeViewModel
import ch.hslu.mobpro.mypokedex.model.PokemonDetailViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class TrainerCardFragment : Fragment() {
    private var _binding: FragmentTrainerCardBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var dialogBuilder: AlertDialog? = null

    //CREATE FAKE FAVORITE LIST
    private val favoritePokemonIds = listOf(1, 4, 7, 25, 151)

    private val detailViewModel: PokemonDetailViewModel by activityViewModels()
    private val pokeViewModel: PokeViewModel by viewModels()

    private lateinit var adapter: PokemonListAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var trainerName: String? = null
    private var trainerID: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        pokeViewModel.requestFavoritesPokeList(favoritePokemonIds)
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
        _binding = FragmentTrainerCardBinding.inflate(inflater, container, false)

        // Initialize shared preferences
        sharedPreferences =
            requireActivity().getSharedPreferences("pokedex_preferencesTEST", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        trainerName = sharedPreferences.getString("trainer_name", null)
        trainerID = sharedPreferences.getString("trainer_id", null)

        //if no trainerID yet, create and save one
        if (trainerID == null) {
            //trainer ID is not saved in shared preferences, generate and save it
            val random = Random()
            val newTrainerID = String.format("%05d", random.nextInt(100000))
            binding.idTrainer.text = newTrainerID
            editor.putString("trainer_id", newTrainerID).apply()
        }

        //show number of favorites in the fragment
        binding.numberFavorites.text = favoritePokemonIds.size.toString()

        //Loads favorites from a list
        //TODO FAKE FAVORITE LIST !!  CHANGE
        getFavoritePokemon()

        return binding.root
    }

    /*
    --> MAIN CODE
     */
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (trainerName != null) {
            binding.nameInput.setText(trainerName)
        }

        //TRAINERID

        if (trainerID != null) {
            //trainer ID is already saved in shared preferences, use it
            binding.idTrainer.text = trainerID
        } else {

        }

        //add time
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        binding.time.text = currentTime

    }

    private fun getFavoritePokemon() {
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

        //get reference from input name
        val refNewTrainerName = binding.nameInput

        //retrieve proper text from it
        val newTrainerName = refNewTrainerName.text.toString()

        if (trainerName == null || trainerName != newTrainerName) {
            //save the name to shared prefs
            editor.putString("trainer_name", newTrainerName).apply()
        }
        _binding = null
    }

}

