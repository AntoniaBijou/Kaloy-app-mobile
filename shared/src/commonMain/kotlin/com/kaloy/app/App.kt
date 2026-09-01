package com.kaloy.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kaloy.app.ui.navigation.KaloyRoute
import com.kaloy.app.ui.screens.ArtistDetailScreen
import com.kaloy.app.ui.screens.HomeScreen
import com.kaloy.app.ui.screens.SearchScreen
import com.kaloy.app.ui.theme.KaloyTheme

@Composable
fun App() {
    KaloyTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = KaloyRoute.Home
            ) {
                // Écran d'accueil
                composable<KaloyRoute.Home> {
                    HomeScreen(
                        onArtistClick = { artistId ->
                            navController.navigate(KaloyRoute.ArtistDetail(artistId))
                        },
                        onAlbumClick = { albumId ->
                            navController.navigate(KaloyRoute.AlbumDetail(albumId))
                        },
                        onSongClick = { songId ->
                            navController.navigate(KaloyRoute.SongDetail(songId))
                        },
                        onSearchClick = {
                            navController.navigate(KaloyRoute.Search)
                        }
                    )
                }

                // Écran de recherche
                composable<KaloyRoute.Search> {
                    SearchScreen(
                        onBackClick = { navController.navigateUp() },
                        onArtistClick = { artistId ->
                            navController.navigate(KaloyRoute.ArtistDetail(artistId))
                        },
                        onSongClick = { songId ->
                            navController.navigate(KaloyRoute.SongDetail(songId))
                        }
                    )
                }

                // Détail Artiste
                composable<KaloyRoute.ArtistDetail> { backStackEntry ->
                    val route: KaloyRoute.ArtistDetail = backStackEntry.toRoute()
                    ArtistDetailScreen(
                        artistId = route.artistId,
                        onBackClick = { navController.navigateUp() },
                        onAlbumClick = { albumId ->
                            navController.navigate(KaloyRoute.AlbumDetail(albumId))
                        },
                        onSongClick = { songId ->
                            navController.navigate(KaloyRoute.SongDetail(songId))
                        }
                    )
                }
                
                // TODO: Implémenter ces écrans plus tard
                composable<KaloyRoute.AlbumDetail> {
                    // AlbumDetailScreen(...)
                }
                
                composable<KaloyRoute.SongDetail> {
                    // SongDetailScreen(...)
                }
            }
        }
    }
}