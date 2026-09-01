package com.kaloy.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaloy.app.data.api.ArtistSearch
import com.kaloy.app.data.api.KaloyApi
import com.kaloy.app.data.model.Artist
import com.kaloy.app.data.model.Song
import com.kaloy.app.ui.components.*
import com.kaloy.app.ui.theme.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// ============================================================
// ViewModel
// ============================================================

class SearchViewModel : ViewModel() {
    private val api = KaloyApi()
    private var searchJob: Job? = null

    var query by mutableStateOf("")
        private set
    var artists by mutableStateOf<List<Artist>>(emptyList())
        private set
    var songs by mutableStateOf<List<Song>>(emptyList())
        private set
    var isSearching by mutableStateOf(false)
        private set
    var hasSearched by mutableStateOf(false)
        private set

    fun onQueryChange(newQuery: String) {
        query = newQuery
        // Debounce: attend 400ms après la dernière frappe
        searchJob?.cancel()
        if (newQuery.length >= 2) {
            searchJob = viewModelScope.launch {
                delay(400)
                performSearch(newQuery)
            }
        } else {
            artists = emptyList()
            songs = emptyList()
            hasSearched = false
        }
    }

    private suspend fun performSearch(searchQuery: String) {
        isSearching = true
        try {
            // Recherche artistes
            val artistResult = try {
                api.searchArtists(ArtistSearch(stageName = searchQuery), size = 20)
            } catch (_: Exception) { null }
            artists = artistResult?.data?.content ?: emptyList()

            // TODO: Quand le backend supportera la recherche de chansons, ajouter ici
            songs = emptyList()

            hasSearched = true
        } catch (_: Exception) {
            // Silently handle
        } finally {
            isSearching = false
        }
    }
}

// ============================================================
// Écran de recherche
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onBackClick: () -> Unit,
    onArtistClick: (Long) -> Unit,
    onSongClick: (Long) -> Unit,
    viewModel: SearchViewModel = viewModel { SearchViewModel() }
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
    ) {
        // ---- Barre de recherche ----
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Text("←", fontSize = 24.sp, color = KaloyTextPrimary)
            }

            OutlinedTextField(
                value = viewModel.query,
                onValueChange = { viewModel.onQueryChange(it) },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        "Rechercher...",
                        color = KaloyTextMuted
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = KaloyPurple,
                    unfocusedBorderColor = KaloyDarkElevated,
                    focusedTextColor = KaloyTextPrimary,
                    unfocusedTextColor = KaloyTextPrimary,
                    cursorColor = KaloyPurple,
                    focusedContainerColor = KaloyDarkCard,
                    unfocusedContainerColor = KaloyDarkCard
                )
            )
        }

        // ---- Résultats ----
        when {
            viewModel.isSearching -> {
                LoadingIndicator()
            }
            !viewModel.hasSearched -> {
                // État initial
                EmptyState(message = "Tapez au moins 2 caractères pour rechercher")
            }
            viewModel.artists.isEmpty() && viewModel.songs.isEmpty() -> {
                EmptyState(message = "Aucun résultat pour \"${viewModel.query}\"")
            }
            else -> {
                LazyColumn {
                    // Artistes trouvés
                    if (viewModel.artists.isNotEmpty()) {
                        item {
                            SectionHeader(title = "🎤 Artistes (${viewModel.artists.size})")
                        }
                        items(
                            items = viewModel.artists,
                            key = { it.id }
                        ) { artist ->
                            ArtistSearchRow(
                                artist = artist,
                                onClick = { onArtistClick(artist.id) }
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = KaloyDarkElevated
                            )
                        }
                    }

                    // Chansons trouvées
                    if (viewModel.songs.isNotEmpty()) {
                        item {
                            SectionHeader(title = "🎵 Chansons (${viewModel.songs.size})")
                        }
                        items(
                            count = viewModel.songs.size,
                            key = { viewModel.songs[it].id }
                        ) { index ->
                            SongRow(
                                song = viewModel.songs[index],
                                index = index,
                                onClick = { onSongClick(viewModel.songs[index].id) }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

// ============================================================
// Ligne de résultat artiste
// ============================================================

@Composable
private fun ArtistSearchRow(
    artist: Artist,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar mini
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.linearGradient(
                        colors = listOf(KaloyPurple, KaloyPink)
                    ),
                    shape = androidx.compose.foundation.shape.CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = artist.stageName.take(2).uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                color = androidx.compose.ui.graphics.Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = artist.stageName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = KaloyTextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                if (artist.isCertified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("✓", color = KaloyCyan, fontSize = 14.sp)
                }
            }
            Text(
                text = artist.artistType?.name ?: "Artiste",
                style = MaterialTheme.typography.bodySmall,
                color = KaloyTextSecondary
            )
        }

        TextButton(onClick = onClick) {
            Text("Voir", color = KaloyPurpleLight)
        }
    }
}
