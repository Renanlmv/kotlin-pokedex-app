package br.com.renanlmv.pokedexapp.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.renanlmv.pokedexapp.data.remote.RetrofitInstance
import br.com.renanlmv.pokedexapp.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PokemonGameViewModel : ViewModel() {

    private val repository = PokemonRepository(RetrofitInstance.api)

    private val _uiState = MutableStateFlow(PokemonGameUiState())

    val uiState: StateFlow<PokemonGameUiState> = _uiState.asStateFlow()

    init {
        loadRandomPokemon()
    }

    fun loadRandomPokemon() {

        viewModelScope.launch {

            _uiState.update {
                PokemonGameUiState(isLoading = true)
            }

            try {
                // limite do catálogo de Pokémons que podem aparecer no jogo
                val pokemonId = (1..51).random()
                val pokemon = repository.getPokemon(pokemonId)
                _uiState.update {
                    PokemonGameUiState(pokemon = pokemon)
                }
            } catch (exception: Exception) {
                _uiState.update {
                    PokemonGameUiState(errorMessage = "Não foi possível carregar o Pokémon.")
                }
            }
        }
    }

    fun onGuessChange(value: String) {

        // atualiza o texto no usuário no parâmetro guess
        _uiState.update {
            // it representa PokemonGameUiState
            it.copy(guess = value)
        }
    }

    fun checkAnswer() {
        // elvis expression ?:
        // indica se um valor é nulo ou não
        // A?:B retorna A se A não for nulo e B se A for nulo
        val pokemon = _uiState.value.pokemon ?: return

        // remove espaços do começo e final e deixa o guess em letras minúsculas
        val answer = _uiState.value.guess.trim().lowercase()

        // verifica se o guess corresponde ao nome do Pokémon
        val correct = answer == pokemon.name.lowercase()

        // atualiza os parâmetros answered e isCorrect para indicar resposta correta
        _uiState.update {
            // it representa PokemonGameUiState
            it.copy(answered = true, isCorrect = correct)
        }
    }

    // reinicia o fluxo carregando o próximo Pokémon
    fun nextPokemon() {
        loadRandomPokemon()
    }
}