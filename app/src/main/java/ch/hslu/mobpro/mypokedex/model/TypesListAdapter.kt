package ch.hslu.mobpro.mypokedex.model

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.firstpokedex.model.PokemonListAdapter
import ch.hslu.mobpro.mypokedex.R
import ch.hslu.mobpro.mypokedex.databinding.ListItemLocationBinding
import ch.hslu.mobpro.mypokedex.databinding.ListItemTypeBinding
import ch.hslu.mobpro.mypokedex.network.PokeApiService
import ch.hslu.mobpro.mypokedex.ui.getBackgroundColor
import com.squareup.picasso.Picasso
import java.util.*

class TypesListAdapter(private val context: Context) :
    RecyclerView.Adapter<TypesListAdapter.TypesViewHolder>() {

    private var typeListResponse = emptyList<PokeApiService.TypeDetails>()

    private lateinit var mListener : onItemClickListener

    //interface for click listener in adapter class
    interface onItemClickListener {
        fun onItemClick(typeId: Int)
    }

    fun setOnItemClickListener(listener : onItemClickListener) {
        mListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemTypeBinding.inflate(inflater, parent, false)
        return TypesViewHolder(binding, mListener)
    }

    override fun onBindViewHolder(holder: TypesListAdapter.TypesViewHolder, position: Int) {
        val type = typeListResponse[position]
        holder.bind(type)

    }

    override fun getItemCount(): Int {
        return typeListResponse.size
    }

    // this method updated the adapter's data
    fun updateData(newTypes: List<PokeApiService.TypeDetails>) {
        typeListResponse = newTypes
        notifyDataSetChanged()
    }

    inner class TypesViewHolder(private val binding: ListItemTypeBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
                val typeId = typeListResponse[adapterPosition].id
            }
        }

        fun bind(type: PokeApiService.TypeDetails) {
            // Convert name to title case and remove hyphens
            val name = type.name.toString()
            val nameCapitalized = name.capitalize()
            binding.typeName.text = nameCapitalized

            val typeColor = setTypeColor(name)
            val typeColorConv = ContextCompat.getColor(binding.root.context, typeColor)

            // Apply the background tint to the typeRelativeLayout
            DrawableCompat.setTint(binding.typeRelativeLayout.background, typeColorConv)
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

}
