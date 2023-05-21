package ch.hslu.mobpro.mypokedex.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.mypokedex.R
import ch.hslu.mobpro.mypokedex.network.PokeApiService
//class LocationListAdapter (private val context: Context) : RecyclerView.Adapter<LocationListAdapter.LocationViewHolder>() {
class MovesListAdapter(private val context: Context) : RecyclerView.Adapter<MovesListAdapter.MoveViewHolder>() {

    private var movesList = emptyList<PokeApiService.Move>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoveViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_move, parent, false)
        return MoveViewHolder(view)
    }

    override fun onBindViewHolder(holder: MoveViewHolder, position: Int) {
        val move = movesList[position]
        holder.bind(move)
    }

    override fun getItemCount(): Int {
        return movesList.size
    }

    fun updateData(newMoves: List<PokeApiService.Move>) {
        movesList = newMoves
        notifyDataSetChanged()
    }

    inner class MoveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val moveNameTextView: TextView = itemView.findViewById(R.id.move_name)

        fun bind(move: PokeApiService.Move) {
            moveNameTextView.text = move.name.capitalize()
        }

    }
}