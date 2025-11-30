package com.example.repositories

import com.example.database.Albums
import com.example.database.Artists
import com.example.models.Album
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

class AlbumRepository {

    fun create(album: Album): Album? = transaction {
        val artistExists = Artists.select { Artists.id eq album.artistId }.count() > 0
        if (!artistExists) return@transaction null

        val id = Albums.insert {
            it[title] = album.title
            it[releaseYear] = album.releaseYear
            it[artistId] = album.artistId
            it[createdAt] = Instant.now()
            it[updatedAt] = Instant.now()
        }[Albums.id]

        album.copy(
            id = id,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
    }

    fun findAll(): List<Album> = transaction {
        Albums.selectAll().map { toAlbum(it) }
    }

    fun findById(id: UUID): Album? = transaction {
        Albums.select { Albums.id eq id }
            .map { toAlbum(it) }
            .singleOrNull()
    }

    fun findByArtistId(artistId: UUID): List<Album> = transaction {
        Albums.select { Albums.artistId eq artistId }
            .map { toAlbum(it) }
    }

    fun update(id: UUID, album: Album): Boolean = transaction {

        val artistExists = Artists.select { Artists.id eq album.artistId }.count() > 0
        if (!artistExists) return@transaction false

        Albums.update({ Albums.id eq id }) {
            it[title] = album.title
            it[releaseYear] = album.releaseYear
            it[artistId] = album.artistId
            it[updatedAt] = Instant.now()
        } > 0
    }

    fun delete(id: UUID): Boolean = transaction {
        Albums.deleteWhere { Albums.id eq id } > 0
    }

    private fun toAlbum(row: ResultRow): Album = Album(
        id = row[Albums.id],
        title = row[Albums.title],
        releaseYear = row[Albums.releaseYear],
        artistId = row[Albums.artistId],
        createdAt = row[Albums.createdAt],
        updatedAt = row[Albums.updatedAt]
    )
}