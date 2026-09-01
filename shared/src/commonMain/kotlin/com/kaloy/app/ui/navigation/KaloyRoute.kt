package com.kaloy.app.ui.navigation

import kotlinx.serialization.Serializable

/**
 * Définition des routes de navigation de l'application.
 */
sealed interface KaloyRoute {
    @Serializable
    data object Home : KaloyRoute

    @Serializable
    data class ArtistDetail(val artistId: Long) : KaloyRoute

    @Serializable
    data class AlbumDetail(val albumId: Long) : KaloyRoute

    @Serializable
    data class SongDetail(val songId: Long) : KaloyRoute

    @Serializable
    data object Search : KaloyRoute

    @Serializable
    data class EventDetail(val eventId: Long) : KaloyRoute
}
