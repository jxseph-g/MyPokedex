package ch.hslu.mobpro.mypokedex.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import ch.hslu.mobpro.firstpokedex.model.PokemonListAdapter
import ch.hslu.mobpro.mypokedex.R
import ch.hslu.mobpro.mypokedex.databinding.FragmentPokedexBinding
import ch.hslu.mobpro.mypokedex.databinding.FragmentTypeChartBinding
import ch.hslu.mobpro.mypokedex.model.*
import kotlinx.coroutines.launch
import java.lang.reflect.Type

class TypeChartFragment : Fragment() {

    private lateinit var adapter: TypesListAdapter

    private val pokeViewModel: PokeViewModel by viewModels()

    private val moveViewModel: MoveViewModel by activityViewModels()


    private var _binding: FragmentTypeChartBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

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
        _binding = FragmentTypeChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            pokeViewModel.requestTypesList()
        }
    }

    //MAIN CODE
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter and set the layout manager outside the collect block
        adapter = TypesListAdapter(requireContext())
        binding.typeList.adapter = adapter
        binding.typeList.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                pokeViewModel.typesFlow.collect { types ->
                    // Update the adapter's data instead of creating a new adapter each time
                    adapter.updateData(types)

                    adapter.setOnItemClickListener(object : TypesListAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            // Handle item click
                            val selectedType = types[position]
                            moveViewModel.selectedType = selectedType.id

                            // Navigate to PokemonDetailFragment
                            val transaction = parentFragmentManager.beginTransaction()
                            transaction.setCustomAnimations(
                                R.anim.slide_in,
                                R.anim.fade_out,
                                R.anim.fade_in,
                                R.anim.slide_out
                            )
                            transaction.replace(R.id.fragment_container, MovesByTypeFragment())
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