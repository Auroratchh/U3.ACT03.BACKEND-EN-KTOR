package com.example.repositories

import com.example.database.Albums
import com.example.database.Tracks
import com.example.models.Track
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

class TrackRepository {

    fun create(track: Track): Track? = transaction {
        val albumExists = Albums.select { Albums.id eq track.albumId }.count() > 0
        if (!albumExists) return@transaction null

        val id = Tracks.insert {
            it[title] = track.title
            it[duration] = track.duration
            it[albumId] = track.albumId
            it[createdAt] = Instant.now()
            it[updatedAt] = Instant.now()
        }[Tracks.id]

        track.copy(
            id = id,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
    }

    fun findAll(): List<Track> = transaction {
        Tracks.selectAll().map { toTrack(it) }
    }

    fun findById(id: UUID): Track? = transaction {
        Tracks.select { Tracks.id eq id }
            .map { toTrack(it) }
            .singleOrNull()
    }

    fun findByAlbumId(albumId: UUID): List<Track> = transaction {
        Tracks.select { Tracks.albumId eq albumId }
            .map { toTrack(it) }
    }

    fun update(id: UUID, track: Track): Boolean = transaction {
        val albumExists = Albums.select { Albums.id eq track.albumId }.count() > 0
        if (!albumExists) return@transaction false

        Tracks.update({ Tracks.id eq id }) {
            it[title] = track.title
            it[duration] = track.duration
            it[albumId] = track.albumId
            it[updatedAt] = Instant.now()
        } > 0
    }

    fun delete(id: UUID): Boolean = transaction {
        Tracks.deleteWhere { Tracks.id eq id } > 0
    }

    private fun toTrack(row: ResultRow): Track = Track(
        id = row[Tracks.id],
        title = row[Tracks.title],
        duration = row[Tracks.duration],
        albumId = row[Tracks.albumId],
        createdAt = row[Tracks.createdAt],
        updatedAt = row[Tracks.updatedAt]
    )
}