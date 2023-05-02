package ch.hslu.mobpro.mypokedex.network

import android.graphics.Region
import android.location.Location
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//full URL : https://pokeapi.co/api/v2/pokemon/{id or name}/

/*
This is a helper class that defines the API service interface and methods
to call the API.
 */
interface PokeApiService {

    //with the offset @Query Limit we will get every name / pokemon from
    // pokemon id / pokédex nr 1 to 151 (actually 152 since it starts at zero!) == only GEN 1 !
    @GET("pokemon")
    suspend fun getPokemonList(@Query("limit") limit: Int): Response<PokemonListResponse>

    //Fetch LocationList with Query Limit to Region1!
    @GET("region/{regionId}")
    suspend fun getLocationList(@Path("regionId") regionId: Int): Response<Region>

    @GET("pokemon/{id}")
    suspend fun getPokemonDetails(@Path("id") id: Int): Response<Pokemon>

    //POKEMON
    data class Pokemon(

        var id: Int?,

        @SerializedName("name")
        val name: String?,

        @SerializedName("url")
        val url: String?,

        @SerializedName("base_experience")
        val baseExperience: Int,

        @SerializedName("types")
        val types: List<Type>,

        @SerializedName("stats")
        val stats: List<Stat>,
    )

    data class Type(
        @SerializedName("slot")
        val slot: Int,
        @SerializedName("type")
        val type: TypeDetails
    )

    data class TypeDetails(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
    )

    data class Stat(
        @SerializedName("base_stat")
        val baseStat: Int,
        @SerializedName("effort")
        val effort: Int,
        @SerializedName("stat")
        val stat: StatDetails
    )

    data class StatDetails(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String
    )

    data class PokemonListResponse(
        @SerializedName("results")
        val pokemonList: List<Pokemon>
    )

    //LOCATIONS
    data class Location(
        @SerializedName("name")
        val name: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("id")
        val id: Int
    )

    data class Region(
        @SerializedName("locations")
        val locations: List<Location>
    )

}