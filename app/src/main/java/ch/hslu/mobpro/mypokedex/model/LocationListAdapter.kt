package ch.hslu.mobpro.mypokedex.model

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.mypokedex.databinding.ListItemLocationBinding
import ch.hslu.mobpro.mypokedex.network.PokeApiService
import com.squareup.picasso.Picasso
import java.util.*

class LocationListAdapter (private val context: Context) : RecyclerView.Adapter<LocationListAdapter.LocationViewHolder>() {

    private var locationList = emptyList<PokeApiService.Location>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationListAdapter.LocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemLocationBinding.inflate(inflater, parent, false)
        return LocationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = locationList[position]
        holder.bind(location)
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    // this method updated the adapter's data
    fun updateData(newLocations: List<PokeApiService.Location>) {
        locationList = newLocations
        notifyDataSetChanged()
    }

    inner class LocationViewHolder(private val binding: ListItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(location: PokeApiService.Location) {
            // Convert name to title case and remove hyphens
            val name = location.name.split("-")
                .joinToString(separator = " ") { it.toLowerCase().capitalize() }
            binding.locationName.text = name
        }
    }

}