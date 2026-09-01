package com.kaloy.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ============================================================
// Réponse API générique du backend
// ============================================================

@Serializable
data class RestResponse<T>(
    val status: Int = 0,
    val message: String = "",
    val data: T? = null
)

@Serializable
data class PageResponse<T>(
    val content: List<T> = emptyList(),
    val totalElements: Long = 0,
    val totalPages: Int = 0,
    val number: Int = 0,
    val size: Int = 0,
    val first: Boolean = true,
    val last: Boolean = true,
    val empty: Boolean = true
)

// ============================================================
// Tables de référence
// ============================================================

@Serializable
data class UserRole(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class UserStatuse(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class ArtistType(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class VerificationStatuse(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class VerificationChannel(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class MemberStatuse(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class AudioStorageType(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class Genre(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class InstrumentRole(
    val id: Long = 0,
    val label: String = ""
)

@Serializable
data class EventModerationStatuse(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class ParticipationStatuse(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class MediaType(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class InteractionTarget(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class ReportStatuse(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class PlaylistVisibilitie(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class PlayMode(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class NotificationType(
    val id: Long = 0,
    val name: String = ""
)

@Serializable
data class SubmissionStatuse(
    val id: Long = 0,
    val name: String = ""
)

// ============================================================
// Entités principales
// ============================================================

@Serializable
data class User(
    val id: Long = 0,
    val email: String? = null,
    val phone: String? = null,
    @SerialName("emailVerifiedAt") val emailVerifiedAt: String? = null,
    @SerialName("phoneVerifiedAt") val phoneVerifiedAt: String? = null,
    @SerialName("passwordHash") val passwordHash: String? = null,
    @SerialName("roleidUserRoles") val role: UserRole? = null,
    @SerialName("statusidUserStatuses") val status: UserStatuse? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
)

@Serializable
data class Artist(
    val id: Long = 0,
    @SerialName("useridUsers") val user: User? = null,
    @SerialName("artisttypeidArtistTypes") val artistType: ArtistType? = null,
    @SerialName("stageName") val stageName: String = "",
    @SerialName("activeSinceYear") val activeSinceYear: Int? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    val bio: String? = null,
    @SerialName("verificationstatusidVerificationStatuses") val verificationStatus: VerificationStatuse? = null,
    @SerialName("verifiedAt") val verifiedAt: String? = null,
    @SerialName("isCertified") val isCertified: Boolean = false,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class Album(
    val id: Long = 0,
    @SerialName("artistidArtists") val artist: Artist? = null,
    val title: String = "",
    @SerialName("coverUrl") val coverUrl: String? = null,
    @SerialName("releaseDate") val releaseDate: String? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class Song(
    val id: Long = 0,
    @SerialName("artistidArtists") val artist: Artist? = null,
    @SerialName("albumidAlbums") val album: Album? = null,
    val title: String = "",
    @SerialName("durationSeconds") val durationSeconds: Int? = null,
    @SerialName("releaseDate") val releaseDate: String? = null,
    val language: String = "fr",
    @SerialName("authorComposer") val authorComposer: String? = null,
    @SerialName("storagetypeidAudioStorageTypes") val storageType: AudioStorageType? = null,
    @SerialName("audioUrl") val audioUrl: String? = null,
    @SerialName("videoUrl") val videoUrl: String? = null,
    val lyrics: String? = null,
    @SerialName("isDownloadable") val isDownloadable: Boolean = true,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class SongGenre(
    val id: Long = 0,
    @SerialName("songidSongs") val song: Song? = null,
    @SerialName("genreidGenres") val genre: Genre? = null
)

@Serializable
data class ArtistGroupMember(
    val id: Long = 0,
    @SerialName("groupartistidArtists") val groupArtist: Artist? = null,
    @SerialName("memberartistidArtists") val memberArtist: Artist? = null,
    @SerialName("fullName") val fullName: String? = null,
    @SerialName("roleinstrumentidInstrumentRoles") val roleInstrument: InstrumentRole? = null,
    @SerialName("photoUrl") val photoUrl: String? = null,
    @SerialName("statusidMemberStatuses") val status: MemberStatuse? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

// ============================================================
// Événements & Concerts
// ============================================================

@Serializable
data class Venue(
    val id: Long = 0,
    val name: String = "",
    val location: String? = null
)

@Serializable
data class Event(
    val id: Long = 0,
    val name: String = "",
    val description: String? = null,
    @SerialName("startDate") val startDate: String? = null,
    @SerialName("endDate") val endDate: String? = null,
    @SerialName("createdbyartistidArtists") val createdByArtist: Artist? = null,
    @SerialName("moderationstatusidEventModerationStatuses") val moderationStatus: EventModerationStatuse? = null,
    @SerialName("reviewedAt") val reviewedAt: String? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class Concert(
    val id: Long = 0,
    @SerialName("eventidEvents") val event: Event? = null,
    val title: String? = null,
    val description: String? = null,
    @SerialName("artistidArtists") val artist: Artist? = null,
    @SerialName("venueidVenues") val venue: Venue? = null,
    @SerialName("startTime") val startTime: String? = null,
    @SerialName("endTime") val endTime: String? = null,
    @SerialName("statusidParticipationStatuses") val status: ParticipationStatuse? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

// ============================================================
// Interactions sociales
// ============================================================

@Serializable
data class Follow(
    val id: Long = 0,
    @SerialName("clientuseridUsers") val clientUser: User? = null,
    @SerialName("artistidArtists") val artist: Artist? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class Comment(
    val id: Long = 0,
    @SerialName("authoruseridUsers") val authorUser: User? = null,
    @SerialName("targettypeidInteractionTargets") val targetType: InteractionTarget? = null,
    @SerialName("targetId") val targetId: Long = 0,
    val content: String = "",
    @SerialName("isHidden") val isHidden: Boolean = false,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class Like(
    val id: Long = 0,
    @SerialName("useridUsers") val user: User? = null,
    @SerialName("targettypeidInteractionTargets") val targetType: InteractionTarget? = null,
    @SerialName("targetId") val targetId: Long = 0,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class Report(
    val id: Long = 0,
    @SerialName("reporteruseridUsers") val reporterUser: User? = null,
    @SerialName("targettypeidInteractionTargets") val targetType: InteractionTarget? = null,
    @SerialName("targetId") val targetId: Long = 0,
    val reason: String? = null,
    @SerialName("statusidReportStatuses") val status: ReportStatuse? = null,
    @SerialName("createdAt") val createdAt: String? = null
)

// ============================================================
// Playlists
// ============================================================

@Serializable
data class Playlist(
    val id: Long = 0,
    @SerialName("owneruseridUsers") val ownerUser: User? = null,
    val name: String = "",
    @SerialName("visibilityidPlaylistVisibilities") val visibility: PlaylistVisibilitie? = null,
    @SerialName("shareToken") val shareToken: String? = null,
    @SerialName("createdAt") val createdAt: String? = null,
    @SerialName("updatedAt") val updatedAt: String? = null
)

@Serializable
data class PlaylistSong(
    val id: Long = 0,
    @SerialName("playlistidPlaylists") val playlist: Playlist? = null,
    @SerialName("songidSongs") val song: Song? = null,
    val position: Int = 0,
    @SerialName("addedAt") val addedAt: String? = null
)

@Serializable
data class EditorialPlaylist(
    val id: Long = 0,
    @SerialName("artistidArtists") val artist: Artist? = null,
    val title: String = "",
    val description: String? = null,
    @SerialName("coverUrl") val coverUrl: String? = null,
    @SerialName("isFeatured") val isFeatured: Boolean = false,
    @SerialName("createdAt") val createdAt: String? = null
)

// ============================================================
// Historique & Notifications
// ============================================================

@Serializable
data class ListeningHistory(
    val id: Long = 0,
    @SerialName("useridUsers") val user: User? = null,
    @SerialName("songidSongs") val song: Song? = null,
    @SerialName("playmodeidPlayModes") val playMode: PlayMode? = null,
    @SerialName("listenedAt") val listenedAt: String? = null
)

@Serializable
data class SearchHistory(
    val id: Long = 0,
    @SerialName("useridUsers") val user: User? = null,
    @SerialName("queryText") val queryText: String = "",
    @SerialName("searchedAt") val searchedAt: String? = null
)

@Serializable
data class Notification(
    val id: Long = 0,
    @SerialName("useridUsers") val user: User? = null,
    @SerialName("typeidNotificationTypes") val type: NotificationType? = null,
    val content: String = "",
    @SerialName("isRead") val isRead: Boolean = false,
    @SerialName("createdAt") val createdAt: String? = null
)

@Serializable
data class NotificationPreference(
    val id: Long = 0,
    @SerialName("useridUsers") val user: User? = null,
    @SerialName("notificationtypeidNotificationTypes") val notificationType: NotificationType? = null,
    @SerialName("isEnabled") val isEnabled: Boolean = true
)
