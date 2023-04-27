package ch.hslu.mobpro.firstpokedex.model

import com.google.gson.annotations.SerializedName

data class Pokemon(

    var id: Int?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("url")
    val url: String?
)

