package br.com.renanlmv.pokedexapp.ui.game

import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage

@Composable
fun PokemonGameScreen(viewModel: PokemonGameViewModel = viewModel()) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold() { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Quem é esse Pokémon?",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }

                uiState.errorMessage != null -> {

                    Text(
                        text = uiState.errorMessage!!,
                        color = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        viewModel.nextPokemon()
                    }) {
                        Text("Tentar novamente")
                    }
                }

                uiState.pokemon != null -> {

                    PokemonGameContent(
                        uiState = uiState,
                        onGuessChange = viewModel::onGuessChange,
                        onCheckAnswer = viewModel::checkAnswer,
                        onNextPokemon = viewModel::nextPokemon
                    )
                }
            }
        }

    }
}

@Composable
private fun PokemonGameContent(
    uiState: PokemonGameUiState,
    onGuessChange: (String) -> Unit,
    onCheckAnswer: () -> Unit,
    onNextPokemon: () -> Unit
) {
    val pokemon = uiState.pokemon ?: return

    val colorFilter =
        if(uiState.answered) {
            null
        } else {
            ColorFilter.tint(
                MaterialTheme.colorScheme.onSurface
            )
        }
    AsyncImage(
        model = pokemon.sprites.other.officialArtwork.frontDefault,
        contentDescription = "Imagem do Pokémon",
        modifier = Modifier.size(240.dp),
        colorFilter = colorFilter
    )

    Spacer(modifier = Modifier.height(32.dp))

    if (!uiState.answered) {

        OutlinedTextField(
            value = uiState.guess,
            onValueChange = onGuessChange,
            label = {
                Text("Nome do Pokémon")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    if(uiState.guess.isNotBlank()) {
                        onCheckAnswer()
                    }
                }
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onCheckAnswer,
            enabled = uiState.guess.isNotBlank()
        ) {
            Text("Adivinhar")
        }
    } else {
        if (uiState.isCorrect) {
            Text(
                text = "Você acertou!",
                color = Color(0xFF2E7D32),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            Text(
                text = "Você errou!",
                color = MaterialTheme.colorScheme.error,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Era ${pokemon.name.replaceFirstChar { it.uppercase() }}!",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onNextPokemon) {
            Text("Próximo Pokémon")
        }
    }
}
