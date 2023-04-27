package ch.hslu.mobpro.firstpokedex.model

import com.google.gson.annotations.SerializedName

data class PokemonListResponse(
    @SerializedName("results")
    val pokemonList: List<Pokemon>
)
