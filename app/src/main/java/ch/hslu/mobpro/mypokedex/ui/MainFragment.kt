package ch.hslu.mobpro.mypokedex.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import ch.hslu.mobpro.mypokedex.R
import ch.hslu.mobpro.mypokedex.databinding.FragmentMainBinding
import ch.hslu.mobpro.mypokedex.ui.PokedexFragment
import com.squareup.picasso.Picasso
import kotlin.random.Random

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var dialogBuilder: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //On App start, create a random number to display a random pokemon picture on the home screen
        var randomNr = Random.nextInt(1, 152)

        //Load pokemon nr with randomNr with Picasso
        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$randomNr.png")
            .into(binding.pokemonHeaderImage)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
    --> MAIN CODE
     */
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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


        //Add more buttons below...
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}