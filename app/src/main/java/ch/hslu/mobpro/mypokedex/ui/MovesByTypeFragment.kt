package ch.hslu.mobpro.mypokedex.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import ch.hslu.mobpro.firstpokedex.model.PokemonListAdapter
import ch.hslu.mobpro.mypokedex.R
import ch.hslu.mobpro.mypokedex.databinding.FragmentMovesByTypeBinding
import ch.hslu.mobpro.mypokedex.databinding.FragmentPokedexBinding
import ch.hslu.mobpro.mypokedex.model.MoveViewModel
import ch.hslu.mobpro.mypokedex.model.MovesListAdapter
import ch.hslu.mobpro.mypokedex.model.PokeViewModel
import kotlinx.coroutines.launch

class MovesByTypeFragment : Fragment() {

    private lateinit var adapter: MovesListAdapter

    private val pokeViewModel: PokeViewModel by viewModels()

    private val moveViewModel: MoveViewModel by activityViewModels()

    private var _binding: FragmentMovesByTypeBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val selectedType: Int? by lazy {
        arguments?.getInt("selectedTypeId")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        val type = moveViewModel.selectedType?.toInt()
        val typeName = type?.let { getTypeNameById(it) }

        val typeColor = typeName?.let { setTypeColor(it) }
        val typeColorConv = typeColor?.let { ContextCompat.getColor(binding.root.context, it) }

        Log.e("MovesId", "Retrieved ${type}, ${typeName}")

        // Apply the background tint to the typeRelativeLayout
        if (typeColorConv != null) {
            binding.movesList.setBackgroundColor(typeColorConv)
        }
        moveViewModel.selectedType?.let { pokeViewModel.requestMovesbyType(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMovesByTypeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MovesListAdapter(requireContext())
        binding.movesList.adapter = adapter
        binding.movesList.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                pokeViewModel.moveByTypeFlow.collect { move ->
                    // Update the adapter's data instead of creating a new adapter each time
                    adapter.updateData(move)
                }
            }
        }
    }

    fun getTypeNameById(typeId: Int): String {
        return when (typeId) {
            1 -> "normal"
            2 -> "fighting"
            3 -> "flying"
            4 -> "poison"
            5 -> "ground"
            6 -> "rock"
            7 -> "bug"
            8 -> "ghost"
            9 -> "steel"
            10 -> "fire"
            11 -> "water"
            12 -> "grass"
            13 -> "electric"
            14 -> "psychic"
            15 -> "dragon"
            16 -> "dark"
            17 -> "steel"
            18 -> "fairy"
            else -> ""
        }
    }

    private fun setTypeColor(typeName: String): Int {
        return when (typeName) {
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
}