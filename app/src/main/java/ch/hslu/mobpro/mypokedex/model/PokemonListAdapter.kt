package ch.hslu.mobpro.firstpokedex.model

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.mypokedex.R
import ch.hslu.mobpro.mypokedex.databinding.ListItemPokemonBinding
import ch.hslu.mobpro.mypokedex.network.PokeApiService
import com.squareup.picasso.Picasso

/*
Recycler Adapter Class

This class gets the pokedex id nr and name and puts it into the recycler view --> list_item_pokemon.xml
 */

class PokemonListAdapter(private var pokemonList: List<PokeApiService.Pokemon>) : RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder>() {

    private lateinit var mListener : onItemClickListener

    private var favoriteList: MutableList<String> = mutableListOf()

    //interface for click listener in adapter class
    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemPokemonBinding.inflate(inflater, parent, false)

        // Initialize shared preferences
        val sharedPreferences =
            parent.context.getSharedPreferences("pokedex_preferencesTEST", Context.MODE_PRIVATE)
        favoriteList = sharedPreferences.getStringSet("favoriteList", setOf<String>())?.toMutableList() ?: mutableListOf()

        return PokemonViewHolder(binding, mListener)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.bind(pokemon)
    }

    // this method updated the adapter's data
    fun updateData(newPokemons: List<PokeApiService.Pokemon>) {
        pokemonList = newPokemons
        notifyDataSetChanged()
    }


    inner class PokemonViewHolder(private val binding: ListItemPokemonBinding, listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
                val pokemonName = pokemonList[adapterPosition].name
            }
        }
        fun bind(pokemon: PokeApiService.Pokemon) {
            val name = pokemon.name?.capitalize()

            // Format the Pokedex ID with leading zeros
            val pokedexId = pokemon.id?.toString()?.padStart(3, '0')

            //check if Pokemon is already in the favorites
            var isFavorite = favoriteList.contains(pokemon.id?.toString())

            if (isFavorite){
                binding.favoriteStar.visibility = View.VISIBLE
            } else if (!isFavorite) {
                binding.favoriteStar.visibility = View.GONE
            }

            binding.pokemonName.text = name
            binding.pokedexNr.text = pokedexId
            Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.id}.png")
                .into(binding.pokemonImage)
        }
    }
}
