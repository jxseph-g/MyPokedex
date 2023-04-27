package ch.hslu.mobpro.mypokedex.model

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import ch.hslu.mobpro.mypokedex.R
import ch.hslu.mobpro.mypokedex.databinding.ListItemLocationBinding
import ch.hslu.mobpro.mypokedex.network.PokeApiService
import com.squareup.picasso.Picasso
import java.util.*

class LocationListAdapter (private val context: Context) : RecyclerView.Adapter<LocationListAdapter.LocationViewHolder>() {

    private var locationList = emptyList<PokeApiService.Location>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationListAdapter.LocationViewHolder {
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

    inner class LocationViewHolder(private val binding: ListItemLocationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(location: PokeApiService.Location) {
            // Convert name to title case and remove hyphens
            val name = location.name.replace("-", " ").split(" ").joinToString(" ") { it.capitalize() }
            binding.locationName.text = name

            val formatted = name.replace(" ", "_").split("_").joinToString("_") { it.capitalize() }

            // Open browser with the corresponding page when clicked
            binding.root.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://bulbapedia.bulbagarden.net/wiki/${formatted}"))
                binding.root.context.startActivity(intent)
            }
        }
    }

}