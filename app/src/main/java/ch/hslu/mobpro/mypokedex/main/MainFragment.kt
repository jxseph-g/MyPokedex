package ch.hslu.mobpro.mypokedex.main

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

        var randomNr = Random.nextInt(1, 152)

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

    //MAIN CODE
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pokedexButton.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, PokedexFragment())
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}