package ch.hslu.mobpro.mypokedex.network

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
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
}

data class Pokemon(

    var id: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("url")
    val url: String?
)

data class PokemonListResponse(
    @SerializedName("results")
    val pokemonList: List<Pokemon>
)