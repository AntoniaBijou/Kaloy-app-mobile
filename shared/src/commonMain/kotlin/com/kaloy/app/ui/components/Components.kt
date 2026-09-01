package com.kaloy.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kaloy.app.data.model.Artist
import com.kaloy.app.data.model.Album
import com.kaloy.app.data.model.Song
import com.kaloy.app.data.model.EditorialPlaylist
import com.kaloy.app.ui.theme.*

// ============================================================
// Carte Artiste
// ============================================================

@Composable
fun ArtistCard(
    artist: Artist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = KaloyDarkCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar circulaire
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(KaloyPurple, KaloyPink)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = artist.stageName.take(2).uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = artist.stageName,
                style = MaterialTheme.typography.bodyMedium,
                color = KaloyTextPrimary,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            Text(
                text = artist.artistType?.name ?: "Artiste",
                style = MaterialTheme.typography.bodySmall,
                color = KaloyTextSecondary,
                maxLines = 1
            )
            
            if (artist.isCertified) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "✓ Certifié",
                    style = MaterialTheme.typography.labelSmall,
                    color = KaloyCyan
                )
            }
        }
    }
}

// ============================================================
// Carte Album
// ============================================================

@Composable
fun AlbumCard(
    album: Album,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = KaloyDarkCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Pochette album (placeholder gradient)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                KaloyPurpleDark,
                                KaloyPink.copy(alpha = 0.7f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "♪",
                    fontSize = 40.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
            
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = album.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = KaloyTextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                Text(
                    text = album.artist?.stageName ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = KaloyTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                
                album.releaseDate?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelSmall,
                        color = KaloyTextMuted
                    )
                }
            }
        }
    }
}

// ============================================================
// Ligne chanson
// ============================================================

@Composable
fun SongRow(
    song: Song,
    index: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Numéro
        Text(
            text = "${index + 1}",
            style = MaterialTheme.typography.bodyMedium,
            color = KaloyTextMuted,
            modifier = Modifier.width(32.dp)
        )
        
        // Icône miniature
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    Brush.linearGradient(
                        colors = listOf(KaloyPurple.copy(alpha = 0.5f), KaloyPink.copy(alpha = 0.3f))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("♪", color = Color.White, fontSize = 18.sp)
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Titre et artiste
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = song.title,
                style = MaterialTheme.typography.bodyMedium,
                color = KaloyTextPrimary,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = song.artist?.stageName ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = KaloyTextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        
        // Durée
        song.durationSeconds?.let { seconds ->
            val min = seconds / 60
            val sec = seconds % 60
            Text(
                text = "$min:${sec.toString().padStart(2, '0')}",
                style = MaterialTheme.typography.bodySmall,
                color = KaloyTextMuted
            )
        }
    }
}

// ============================================================
// Carte Playlist Éditoriale
// ============================================================

@Composable
fun EditorialPlaylistCard(
    playlist: EditorialPlaylist,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(200.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = KaloyDarkCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(KaloyCyan.copy(alpha = 0.8f), KaloyPurple.copy(alpha = 0.6f))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("🎵", fontSize = 32.sp)
            }
            
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = playlist.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = KaloyTextPrimary,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                playlist.description?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = KaloyTextSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if (playlist.isFeatured) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "⭐ À la une",
                        style = MaterialTheme.typography.labelSmall,
                        color = KaloyOrange
                    )
                }
            }
        }
    }
}

// ============================================================
// Section header
// ============================================================

@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    action: String? = null,
    onAction: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = KaloyTextPrimary,
            fontWeight = FontWeight.Bold
        )
        if (action != null && onAction != null) {
            TextButton(onClick = onAction) {
                Text(
                    text = action,
                    style = MaterialTheme.typography.labelMedium,
                    color = KaloyPurpleLight
                )
            }
        }
    }
}

// ============================================================
// État de chargement
// ============================================================

@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = KaloyPurple,
            strokeWidth = 3.dp
        )
    }
}

// ============================================================
// État d'erreur
// ============================================================

@Composable
fun ErrorState(
    message: String,
    onRetry: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "⚠️",
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = KaloyTextSecondary
        )
        if (onRetry != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = KaloyPurple
                )
            ) {
                Text("Réessayer")
            }
        }
    }
}

// ============================================================
// État vide
// ============================================================

@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🎵",
            fontSize = 48.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = KaloyTextSecondary
        )
    }
}
