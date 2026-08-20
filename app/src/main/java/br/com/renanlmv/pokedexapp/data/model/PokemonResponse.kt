package br.com.renanlmv.pokedexapp.data.model

import com.google.gson.annotations.SerializedName

data class PokemonResponse(
    val id: Int,
    val name: String,
    val sprites: PokemonSprites
)

data class PokemonSprites(

    @SerializedName("front_default")
    val frontDefault: String?,
    val other: PokemonOtherSprites
)

data class PokemonOtherSprites (

    @SerializedName("official-artwork")
    val officialArtwork: PokemonOfficialArtwork
)

data class PokemonOfficialArtwork(

    @SerializedName("front_default")
    val frontDefault: String?
)

