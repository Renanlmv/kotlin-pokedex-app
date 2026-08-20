package br.com.renanlmv.pokedexapp.data.repository

import br.com.renanlmv.pokedexapp.data.model.PokemonResponse
import br.com.renanlmv.pokedexapp.data.remote.PokemonApi

class PokemonRepository(
    private val api: PokemonApi
) {
    suspend fun getPokemon(
        id: Int
    ): PokemonResponse {
        return api.getPokemon(id);
    }
}