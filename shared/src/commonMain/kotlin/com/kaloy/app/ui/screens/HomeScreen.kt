package com.kaloy.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kaloy.app.data.api.KaloyApi
import com.kaloy.app.data.model.*
import com.kaloy.app.ui.components.*
import com.kaloy.app.ui.theme.*
import kotlinx.coroutines.launch

// ============================================================
// ViewModel
// ============================================================

class HomeViewModel : ViewModel() {
    private val api = KaloyApi()

    var artists by mutableStateOf<List<Artist>>(emptyList())
        private set
    var albums by mutableStateOf<List<Album>>(emptyList())
        private set
    var songs by mutableStateOf<List<Song>>(emptyList())
        private set
    var editorialPlaylists by mutableStateOf<List<EditorialPlaylist>>(emptyList())
        private set
    var recentListens by mutableStateOf<List<ListeningHistory>>(emptyList())
        private set
    var isLoading by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                // Charger en parallèle
                val artistsResult = try { api.getArtists(size = 20) } catch (e: Exception) { null }
                val albumsResult = try { api.getAlbums(size = 10) } catch (e: Exception) { null }
                val songsResult = try { api.getSongs(size = 15) } catch (e: Exception) { null }
                val playlistsResult = try { api.getEditorialPlaylists(size = 10) } catch (e: Exception) { null }
                
                // Simuler utilisateur ID = 1
                val historyResult = try { 
                    api.getListeningHistory(
                        query = com.kaloy.app.data.api.ListeningHistorySearchDto(com.kaloy.app.data.api.UserIdDto(1L)), 
                        size = 10
                    ) 
                } catch (e: Exception) { null }

                artists = artistsResult?.data?.content ?: emptyList()
                albums = albumsResult?.data?.content ?: emptyList()
                songs = songsResult?.data?.content ?: emptyList()
                editorialPlaylists = playlistsResult?.data?.content ?: emptyList()
                
                // Garder les chansons uniques dans l'historique
                recentListens = (historyResult?.data?.content ?: emptyList())
                    .distinctBy { it.song?.id }

                if (artists.isEmpty() && albums.isEmpty() && songs.isEmpty()) {
                    error = "Aucune donnée disponible. Vérifiez que le backend est démarré."
                }
            } catch (e: Exception) {
                error = "Erreur de connexion: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}

// ============================================================
// Écran d'accueil
// ============================================================

@Composable
fun HomeScreen(
    onArtistClick: (Long) -> Unit,
    onAlbumClick: (Long) -> Unit,
    onSongClick: (Long) -> Unit,
    onSearchClick: () -> Unit,
    viewModel: HomeViewModel = viewModel { HomeViewModel() }
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // ---- Header avec gradient ----
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                KaloyPurple.copy(alpha = 0.8f),
                                KaloyPink.copy(alpha = 0.4f),
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
                    .padding(16.dp)
                    .safeContentPadding(),
                contentAlignment = Alignment.BottomStart
            ) {
                Column {
                    Text(
                        text = "Bonsoir 🎶",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Découvrez la musique malgache",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // ---- Barre de recherche ----
        item {
            OutlinedButton(
                onClick = onSearchClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = KaloyTextSecondary
                )
            ) {
                Text(
                    text = "🔍 Rechercher artistes, albums, chansons...",
                    modifier = Modifier.fillMaxWidth(),
                    color = KaloyTextMuted
                )
            }
        }

        // ---- Contenu ----
        when {
            viewModel.isLoading -> {
                item { LoadingIndicator() }
            }
            viewModel.error != null -> {
                item {
                    ErrorState(
                        message = viewModel.error!!,
                        onRetry = { viewModel.loadData() }
                    )
                }
            }
            else -> {
                // Récemment écouté
                if (viewModel.recentListens.isNotEmpty()) {
                    item {
                        SectionHeader(title = "🕒 Récemment écouté")
                    }
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(viewModel.recentListens) { historyObj ->
                                historyObj.song?.let { song ->
                                    Column(
                                        modifier = Modifier
                                            .width(110.dp)
                                            .clickable { onSongClick(song.id) },
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(100.dp)
                                                .clip(RoundedCornerShape(12.dp))
                                                .background(
                                                    Brush.linearGradient(
                                                        colors = listOf(KaloyPurple, KaloyPink)
                                                    )
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "♪",
                                                color = Color.White,
                                                fontSize = 32.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = song.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = KaloyTextPrimary,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = song.artist?.stageName ?: "",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = KaloyTextSecondary,
                                            maxLines = 1,
                                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
                // Playlists à la une
                if (viewModel.editorialPlaylists.isNotEmpty()) {
                    item {
                        SectionHeader(title = "🔥 À la une")
                    }
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(viewModel.editorialPlaylists) { playlist ->
                                EditorialPlaylistCard(
                                    playlist = playlist,
                                    onClick = { /* TODO: navigation playlist */ }
                                )
                            }
                        }
                    }
                }

                // Artistes populaires
                if (viewModel.artists.isNotEmpty()) {
                    item {
                        SectionHeader(title = "🎤 Artistes")
                    }
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(viewModel.artists) { artist ->
                                ArtistCard(
                                    artist = artist,
                                    onClick = { onArtistClick(artist.id) }
                                )
                            }
                        }
                    }
                }

                // Albums récents
                if (viewModel.albums.isNotEmpty()) {
                    item {
                        SectionHeader(title = "💿 Albums")
                    }
                    item {
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(viewModel.albums) { album ->
                                AlbumCard(
                                    album = album,
                                    onClick = { onAlbumClick(album.id) }
                                )
                            }
                        }
                    }
                }

                // Chansons
                if (viewModel.songs.isNotEmpty()) {
                    item {
                        SectionHeader(title = "🎵 Chansons")
                    }
                    items(
                        count = viewModel.songs.size,
                        key = { viewModel.songs[it].id }
                    ) { index ->
                        val song = viewModel.songs[index]
                        SongRow(
                            song = song,
                            index = index,
                            onClick = { onSongClick(song.id) }
                        )
                        if (index < viewModel.songs.size - 1) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = KaloyDarkElevated
                            )
                        }
                    }
                }

                // Espacement en bas
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}
