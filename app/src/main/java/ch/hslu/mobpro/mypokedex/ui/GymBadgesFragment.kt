package ch.hslu.mobpro.mypokedex.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import ch.hslu.mobpro.mypokedex.R
import ch.hslu.mobpro.mypokedex.databinding.FragmentGymBadgesBinding
import ch.hslu.mobpro.mypokedex.databinding.FragmentMainBinding
import ch.hslu.mobpro.mypokedex.databinding.FragmentTrainerCardBinding
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class GymBadgesFragment : Fragment() {

    private var _binding: FragmentGymBadgesBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private var dialogBuilder: AlertDialog? = null

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    private var trainerName: String? = null

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
        _binding = FragmentGymBadgesBinding.inflate(inflater, container, false)

        // Initialize shared preferences
        sharedPreferences = requireActivity().getSharedPreferences("pokedex_preferencesTEST", Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
        trainerName = sharedPreferences.getString("trainer_name", null)
        //sharedPreferences.getStringSet()

        return binding.root
    }

    /*
    --> MAIN CODE
     */
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //load up and display trainer name, if empty, then "Hello, Trainer!"
        if (trainerName != null) {
            binding.name.setText(trainerName)
        } else {
            binding.name.text ="TRAINER"
        }

        //add time
        val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
        binding.time.text = currentTime

        //BASE URL
        //https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/badges/$id.png

        //Load pokemon nr with randomNr with Picasso
        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/badges/1.png")
            .into(binding.badge1)

        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/badges/2.png")
            .into(binding.badge2)

        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/badges/3.png")
            .into(binding.badge3)

        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/badges/4.png")
            .into(binding.badge4)

        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/badges/5.png")
            .into(binding.badge5)

        Picasso.get()
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/badges/6.png")
            .into(binding.badge6)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}