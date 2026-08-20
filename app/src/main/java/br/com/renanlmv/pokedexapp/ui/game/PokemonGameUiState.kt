package br.com.renanlmv.pokedexapp.ui.game

import br.com.renanlmv.pokedexapp.data.model.PokemonResponse

data class PokemonGameUiState (
    val isLoading: Boolean = false,
    val pokemon: PokemonResponse? = null,
    val guess: String = "",
    val answered: Boolean = false,
    val isCorrect: Boolean = false,
    val errorMessage: String? = null
)