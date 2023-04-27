package ch.hslu.mobpro.firstpokedex.model

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.mypokedex.databinding.ListItemPokemonBinding
import ch.hslu.mobpro.mypokedex.network.Pokemon
import com.squareup.picasso.Picasso

/*
Recycler Adapter Class

This class gets the pokedex id nr and name and puts it into the recycler view --> list_item_pokemon.xml
 */

class PokemonListAdapter(private var pokemonList: List<Pokemon>) : RecyclerView.Adapter<PokemonListAdapter.PokemonViewHolder>() {

    private lateinit var mListener : onItemClickListener

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
    fun updateData(newPokemons: List<Pokemon>) {
        pokemonList = newPokemons
        notifyDataSetChanged()
    }


    inner class PokemonViewHolder(private val binding: ListItemPokemonBinding, listener: onItemClickListener) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
                val pokemonName = pokemonList[adapterPosition].name
                Toast.makeText(itemView.context, "You clicked on $pokemonName", Toast.LENGTH_SHORT).show()
            }
        }

        fun bind(pokemon: Pokemon) {
            //get pkmn names and ids and put it with binding into recycler view
            binding.pokemonName.text = "${pokemon.name}"
            binding.pokedexNr.text = "${pokemon.id}"
            Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.id}.png")
                .into(binding.pokemonImage)
        }
    }
}
