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
    private val retrofit = Retrofit.Builder().client(OkHttpClient().newBuilder().build())
        .addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).build()

    //create pokeService with retrofit and thanks to PokeApiService class
    private val pokeService = retrofit.create(PokeApiService::class.java)

    //Pokemon List GEN 1
    //_pokeFlow is a private MutableStateFlow object that holds the list of Pokemon objects
    private val _pokeFlow: MutableStateFlow<List<PokeApiService.Pokemon>> =
        MutableStateFlow(emptyList())

    //pokeFlow: A public read-only Flow object that exposes the list of Pokemon objects to the UI components.
    //everytime _pokeFlow gets updated, the UI gets notified
    val pokeFlow: Flow<List<PokeApiService.Pokemon>> = _pokeFlow

    //Location List GEN 1
    //_locationFlow is a private MutableStateFlow object that holds the list of Location objects
    private val _locationFlow: MutableStateFlow<List<PokeApiService.Location>> =
        MutableStateFlow(emptyList())

    //locationFlow: A public read-only Flow object that exposes the list of Location objects to the UI components.
    val locationFlow: Flow<List<PokeApiService.Location>> = _locationFlow

    private val _pokemonDetailsFlow: MutableStateFlow<PokeApiService.Pokemon?> =
        MutableStateFlow(null)

    val pokemonDetailsFlow: Flow<PokeApiService.Pokemon?> = _pokemonDetailsFlow

    private val _typesFlow: MutableStateFlow<List<PokeApiService.TypeDetails>> = MutableStateFlow(
        emptyList())

    val typesFlow: Flow<List<PokeApiService.TypeDetails>> = _typesFlow

    //launch coroutine with viewModelScope to fetch list of Pokémon from server with getPokeListFromServer()
    //
    fun requestPokeList() {
        viewModelScope.launch {
            val pokemons = getPokeListFromServer()
            pokemons?.let { _pokeFlow.emit(pokemons) }
        }
    }

    fun requestFavoritesPokeList(ids: List<Int>) {
        viewModelScope.launch {
            val pokemons = getPokemonListById(ids)
            pokemons.let { _pokeFlow.emit(pokemons) }
        }
    }

    //launch coroutine with viewModelScope to fetch list of Pokémon from server with getPokeListFromServer()
    //
    suspend fun requestLocationsList(regionId: Int) {
        val locationList = getLocationListFromServer(1)
        locationList?.let { _locationFlow.emit(locationList) }
    }

    suspend fun requestTypesList() {
        viewModelScope.launch {
            val types = getTypesListFromServer()
            types?.let { _typesFlow.emit(types)}
        }
    }

    //API Call to get the list of pokemon
    private suspend fun getPokeListFromServer(): List<PokeApiService.Pokemon>? {
        //The block inside withContext will run on background Thread - not blocking main thread
        return withContext(Dispatchers.IO) {
            val response = pokeService.getPokemonList(151)
            if (response.code() == HttpURLConnection.HTTP_OK) {
                //if HTTP_OK then get the whole response.body = the whole json and iterate over it
                val pokemonListResponse = response.body()
                pokemonListResponse?.pokemonList?.forEach { pokemon ->
                    //"https://pokeapi.co/api/v2/pokemon/1/" --> get the 7th substring with get(6)
                    var id = pokemon.url?.split("/")?.get(6)?.toInt()
                    pokemon.id = id
                }
                pokemonListResponse?.pokemonList
            } else {
                null
            }
        }
    }

    //API Call for the TrainerCard to only get Chosen Pokemon
    private suspend fun getPokemonListById(ids: List<Int>): List<PokeApiService.Pokemon> {
        return withContext(Dispatchers.IO) {
            val pokemonList = mutableListOf<PokeApiService.Pokemon>()
            for (id in ids) {
                val response = pokeService.getPokemonDetails(id)
                if (response.code() == HttpURLConnection.HTTP_OK) {
                    val pokemon = response.body()
                    pokemonList.add(pokemon!!)
                }
            }
            pokemonList
        }
    }

    //API Call and Parsing json from one single Pokemon into Pokemon Object
    suspend fun getPokemonDetails(id: Int): PokeApiService.Pokemon? {
        return withContext(Dispatchers.IO) {
            val response = pokeService.getPokemonDetails(id)
            if (response.code() == HttpURLConnection.HTTP_OK) {
                response.body()
            } else {
                null
            }
        }
    }

    //API call to get pokemon species AKA pokemon description etc.
    suspend fun getPokemonSpecies(id: Int): PokeApiService.PokemonSpecies? {
        return withContext(Dispatchers.IO) {
            val response = pokeService.getPokemonSpecies(id)
            if (response.code() == HttpURLConnection.HTTP_OK) {
                response.body()
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

    private suspend fun getTypesListFromServer() : List<PokeApiService.TypeDetails>? {
        //The block inside withContext will run on background Thread - not blocking main thread
        return withContext(Dispatchers.IO) {
            //Set delimiter to 14 to get all the 15 types from GEN1
            val response = pokeService.getTypeList(14)
            if (response.code() == HttpURLConnection.HTTP_OK) {
                //if HTTP_OK then get the whole response.body = the whole json and iterate over it
                val typelistResponse = response.body()
                typelistResponse?.typesList?.forEach { type ->
                    //"https://pokeapi.co/api/v2/type/1/" --> get the 7th substring with get(6)
                    var id = type.url?.split("/")?.get(6)?.toInt()
                    if (id != null) {
                        type.id = id
                    }
                }
                typelistResponse?.typesList
            } else {
                null
            }
        }
    }
}