package ch.hslu.mobpro.mypokedex.model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ch.hslu.mobpro.mypokedex.network.PokeApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

/*
This class uses the MVVM (Model-View-ViewModel) architecture.
The purpose of this class is to manage and provide data related to Pokémon
to the UI components. It uses the PokeApiService to fetch a list of Pokémon
from an API and expose the fetched data through a Flow object to the UI.
 */

private const val BASE_URL = "https://pokeapi.co/api/v2/"

class PokeViewModel : ViewModel() {

    //create retrofit instance
    private val retrofit = Retrofit.Builder()
        .client(OkHttpClient().newBuilder().build())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    //create pokeService with retrofit and thanks to PokeApiService class
    private val pokeService = retrofit.create(PokeApiService::class.java)

    //Pokemon List GEN 1
    //_pokeFlow is a private MutableStateFlow object that holds the list of Pokemon objects
    private val _pokeFlow: MutableStateFlow<List<PokeApiService.Pokemon>> =
        MutableStateFlow(emptyList())

    //pokeFlow: A public read-only Flow object that exposes the list of Pokemon objects to the UI components.
    val pokeFlow: Flow<List<PokeApiService.Pokemon>> = _pokeFlow

    //Location List GEN 1
//_locationFlow is a private MutableStateFlow object that holds the list of Location objects
    private val _locationFlow: MutableStateFlow<List<PokeApiService.Location>> =
        MutableStateFlow(emptyList())

    //locationFlow: A public read-only Flow object that exposes the list of Location objects to the UI components.
    val locationFlow: Flow<List<PokeApiService.Location>> = _locationFlow

    //launch coroutine with viewModelScope to fetch list of Pokémon from server with getPokeListFromServer()
    //
    fun requestPokeList() {
        viewModelScope.launch {
            val pokemons = getPokeListFromServer()
            pokemons?.let { _pokeFlow.emit(pokemons) }
        }
    }

    //launch coroutine with viewModelScope to fetch list of Pokémon from server with getPokeListFromServer()
    //
    suspend fun requestLocationsList(regionId: Int) {
        val locationList = getLocationListFromServer(1)
        locationList?.let { _locationFlow.emit(locationList)}
    }

    //API Call to get the list of pokemon
    private suspend fun getPokeListFromServer(): List<PokeApiService.Pokemon>? {
        return withContext(Dispatchers.IO) {
            val response = pokeService.getPokemonList(151)
            if (response.code() == HttpURLConnection.HTTP_OK) {
                val pokemonListResponse = response.body()
                pokemonListResponse?.pokemonList?.forEach { pokemon ->
                    var id = pokemon.url?.split("/")?.get(6)?.toInt()
                    pokemon.id = id
                }
                pokemonListResponse?.pokemonList
            } else {
                null
            }
        }
    }

    //API Call to get the list of moves from a pokemon ID
    private suspend fun getPokemonMovesList(id: Int): List<String>? {
        return withContext(Dispatchers.IO) {
            val response = pokeService.getPokemonMoves(id)
            if (response.code() == HttpURLConnection.HTTP_OK) {
                val movesResponse = response.body()
                movesResponse?.moves?.map { it.move }
            } else {
                null
            }
        }
    }

    //API call to get the list of locations
    private suspend fun getLocationListFromServer(regionId: Int): List<PokeApiService.Location>? {
        return withContext(Dispatchers.IO) {
            val response = pokeService.getLocationList(1)
            if (response.code() == HttpURLConnection.HTTP_OK) {
                val region = response.body()
                region?.locations
            } else {
                null
            }
        }
    }
}