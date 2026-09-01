package com.kaloy.app.data.api

import com.kaloy.app.data.model.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

/**
 * Client API pour communiquer avec le backend Mozika.
 * 
 * L'URL de base est configurable. Sur un émulateur Android,
 * utiliser 10.0.2.2 au lieu de localhost.
 */
class KaloyApi(baseUrl: String = DEFAULT_BASE_URL) {

    companion object {
        // Pour émulateur Android : 10.0.2.2 redirige vers localhost du PC
        const val DEFAULT_BASE_URL = "http://10.0.2.2:8087/mozika"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        coerceInputValues = true
        encodeDefaults = true
    }

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(this@KaloyApi.json)
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }

    private val apiBaseUrl = baseUrl

    // ============================================================
    // Artists
    // ============================================================

    suspend fun getArtists(page: Int = 0, size: Int = 10): RestResponse<PageResponse<Artist>> {
        return client.get("$apiBaseUrl/artists") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun getArtistById(id: Long): RestResponse<Artist> {
        return client.get("$apiBaseUrl/artists/$id").body()
    }

    suspend fun getArtistAlbums(artistId: Long, page: Int = 0, size: Int = 10): RestResponse<PageResponse<Album>> {
        return client.get("$apiBaseUrl/artists/$artistId/albums") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun getArtistSongs(artistId: Long, page: Int = 0, size: Int = 10): RestResponse<PageResponse<Song>> {
        return client.get("$apiBaseUrl/artists/$artistId/songs") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun getArtistFollows(artistId: Long, page: Int = 0, size: Int = 10): RestResponse<PageResponse<Follow>> {
        return client.get("$apiBaseUrl/artists/$artistId/follows") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    // ============================================================
    // Albums
    // ============================================================

    suspend fun getAlbums(page: Int = 0, size: Int = 10): RestResponse<PageResponse<Album>> {
        return client.get("$apiBaseUrl/albums") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun getAlbumById(id: Long): RestResponse<Album> {
        return client.get("$apiBaseUrl/albums/$id").body()
    }

    // ============================================================
    // Songs
    // ============================================================

    suspend fun getSongs(page: Int = 0, size: Int = 10): RestResponse<PageResponse<Song>> {
        return client.get("$apiBaseUrl/songs") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun getSongById(id: Long): RestResponse<Song> {
        return client.get("$apiBaseUrl/songs/$id").body()
    }

    // ============================================================
    // Events
    // ============================================================

    suspend fun getEvents(page: Int = 0, size: Int = 10): RestResponse<PageResponse<Event>> {
        return client.get("$apiBaseUrl/events") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun getEventById(id: Long): RestResponse<Event> {
        return client.get("$apiBaseUrl/events/$id").body()
    }

    // ============================================================
    // Editorial Playlists
    // ============================================================

    suspend fun getEditorialPlaylists(page: Int = 0, size: Int = 10): RestResponse<PageResponse<EditorialPlaylist>> {
        return client.get("$apiBaseUrl/editorialplaylists") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    // ============================================================
    // Genres
    // ============================================================

    suspend fun getGenres(page: Int = 0, size: Int = 50): RestResponse<PageResponse<Genre>> {
        return client.get("$apiBaseUrl/genres") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    // ============================================================
    // Search
    // ============================================================

    suspend fun searchArtists(query: ArtistSearch, page: Int = 0, size: Int = 10): RestResponse<PageResponse<Artist>> {
        return client.post("$apiBaseUrl/artists/search") {
            contentType(ContentType.Application.Json)
            parameter("page", page)
            parameter("size", size)
            setBody(query)
        }.body()
    }

    // ============================================================
    // Follows
    // ============================================================

    suspend fun getFollows(page: Int = 0, size: Int = 10): RestResponse<PageResponse<Follow>> {
        return client.get("$apiBaseUrl/follows") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun createFollow(follow: Follow): RestResponse<Follow> {
        return client.post("$apiBaseUrl/follows") {
            contentType(ContentType.Application.Json)
            setBody(follow)
        }.body()
    }

    suspend fun deleteFollow(id: Long): RestResponse<Any> {
        return client.delete("$apiBaseUrl/follows/$id").body()
    }

    // ============================================================
    // Comments
    // ============================================================

    suspend fun getComments(page: Int = 0, size: Int = 10): RestResponse<PageResponse<Comment>> {
        return client.get("$apiBaseUrl/comments") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun createComment(comment: Comment): RestResponse<Comment> {
        return client.post("$apiBaseUrl/comments") {
            contentType(ContentType.Application.Json)
            setBody(comment)
        }.body()
    }

    // ============================================================
    // Likes
    // ============================================================

    suspend fun getLikes(page: Int = 0, size: Int = 10): RestResponse<PageResponse<Like>> {
        return client.get("$apiBaseUrl/likes") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    suspend fun createLike(like: Like): RestResponse<Like> {
        return client.post("$apiBaseUrl/likes") {
            contentType(ContentType.Application.Json)
            setBody(like)
        }.body()
    }

    suspend fun deleteLike(id: Long): RestResponse<Any> {
        return client.delete("$apiBaseUrl/likes/$id").body()
    }

    // ============================================================
    // Notifications
    // ============================================================

    suspend fun getNotifications(page: Int = 0, size: Int = 10): RestResponse<PageResponse<Notification>> {
        return client.get("$apiBaseUrl/notifications") {
            parameter("page", page)
            parameter("size", size)
        }.body()
    }

    // ============================================================
    // Reports
    // ============================================================

    suspend fun createReport(report: Report): RestResponse<Report> {
        return client.post("$apiBaseUrl/reports") {
            contentType(ContentType.Application.Json)
            setBody(report)
        }.body()
    }

    // ============================================================
    // Listening History
    // ============================================================

    suspend fun getListeningHistory(query: ListeningHistorySearchDto, page: Int = 0, size: Int = 10, sortParam: String = "listenedAt,desc"): RestResponse<PageResponse<ListeningHistory>> {
        return client.post("$apiBaseUrl/listeninghistorys/search") {
            contentType(ContentType.Application.Json)
            parameter("page", page)
            parameter("size", size)
            parameter("sortParam", sortParam)
            setBody(query)
        }.body()
    }
}

// DTO de recherche pour POST /artists/search
@kotlinx.serialization.Serializable
data class ArtistSearch(
    val stageName: String? = null,
    val bio: String? = null
)

// DTO de recherche pour POST /listeninghistorys/search
@kotlinx.serialization.Serializable
data class ListeningHistorySearchDto(
    val useridUsers: UserIdDto? = null
)

@kotlinx.serialization.Serializable
data class UserIdDto(
    val id: Long
)

