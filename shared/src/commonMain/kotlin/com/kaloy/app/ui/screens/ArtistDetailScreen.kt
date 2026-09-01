package com.kaloy.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

class ArtistDetailViewModel(private val artistId: Long) : ViewModel() {
    private val api = KaloyApi()

    var artist by mutableStateOf<Artist?>(null)
        private set
    var albums by mutableStateOf<List<Album>>(emptyList())
        private set
    var songs by mutableStateOf<List<Song>>(emptyList())
        private set
    var members by mutableStateOf<List<ArtistGroupMember>>(emptyList())
        private set
    var isLoading by mutableStateOf(true)
        private set
    var error by mutableStateOf<String?>(null)
        private set

    init {
        loadArtist()
    }

    fun loadArtist() {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val artistResult = api.getArtistById(artistId)
                artist = artistResult.data

                val albumsResult = try { api.getArtistAlbums(artistId, size = 20) } catch (_: Exception) { null }
                albums = albumsResult?.data?.content ?: emptyList()

                val songsResult = try { api.getArtistSongs(artistId, size = 30) } catch (_: Exception) { null }
                songs = songsResult?.data?.content ?: emptyList()
            } catch (e: Exception) {
                error = "Impossible de charger l'artiste: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}

// ============================================================
// Écran détail artiste
// ============================================================

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistDetailScreen(
    artistId: Long,
    onBackClick: () -> Unit,
    onAlbumClick: (Long) -> Unit,
    onSongClick: (Long) -> Unit,
    viewModel: ArtistDetailViewModel = viewModel(key = "artist_$artistId") { ArtistDetailViewModel(artistId) }
) {
    when {
        viewModel.isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }
        viewModel.error != null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
            ) {
                ErrorState(
                    message = viewModel.error!!,
                    onRetry = { viewModel.loadArtist() }
                )
            }
        }
        viewModel.artist != null -> {
            val artist = viewModel.artist!!

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                // ---- Header artiste avec gradient ----
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(280.dp)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        KaloyPurple,
                                        KaloyPink.copy(alpha = 0.6f),
                                        MaterialTheme.colorScheme.background
                                    )
                                )
                            )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .safeContentPadding(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Bouton retour
                            IconButton(
                                onClick = onBackClick,
                                colors = IconButtonDefaults.iconButtonColors(
                                    contentColor = Color.White
                                )
                            ) {
                                Text("←", fontSize = 24.sp, color = Color.White)
                            }

                            // Info artiste
                            Row(
                                verticalAlignment = Alignment.Bottom,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Avatar
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .background(
                                            Brush.linearGradient(
                                                colors = listOf(KaloyPurpleDark, KaloyPink)
                                            )
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = artist.stageName.take(2).uppercase(),
                                        fontSize = 36.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = artist.stageName,
                                            style = MaterialTheme.typography.headlineMedium,
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (artist.isCertified) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text("✓", color = KaloyCyan, fontSize = 18.sp)
                                        }
                                    }
                                    Text(
                                        text = artist.artistType?.name ?: "Artiste",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = Color.White.copy(alpha = 0.8f)
                                    )
                                    artist.activeSinceYear?.let {
                                        Text(
                                            text = "Actif depuis $it",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.White.copy(alpha = 0.6f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // ---- Biographie ----
                artist.bio?.let { bio ->
                    if (bio.isNotBlank()) {
                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Biographie",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = KaloyTextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = bio,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = KaloyTextSecondary
                                )
                            }
                        }
                    }
                }

                // ---- Albums ----
                if (viewModel.albums.isNotEmpty()) {
                    item {
                        SectionHeader(title = "💿 Albums (${viewModel.albums.size})")
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

                // ---- Chansons ----
                if (viewModel.songs.isNotEmpty()) {
                    item {
                        SectionHeader(title = "🎵 Chansons (${viewModel.songs.size})")
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
