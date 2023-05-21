package ch.hslu.mobpro.mypokedex.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ch.hslu.mobpro.mypokedex.network.PokeApiService

class MoveViewModel : ViewModel() {
    var selectedType: Int? = null
}