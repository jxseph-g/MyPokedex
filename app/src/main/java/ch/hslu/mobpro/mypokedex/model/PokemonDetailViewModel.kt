package ch.hslu.mobpro.mypokedex.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.hslu.mobpro.mypokedex.network.PokeApiService

class PokemonDetailViewModel : ViewModel() {
    private val selectedPokemon = MutableLiveData<PokeApiService.Pokemon>()

    fun setSelectedPokemon(pokemon: PokeApiService.Pokemon) {
        selectedPokemon.value = pokemon
    }

    fun getSelectedPokemon(): LiveData<PokeApiService.Pokemon> {
        return selectedPokemon
    }
}