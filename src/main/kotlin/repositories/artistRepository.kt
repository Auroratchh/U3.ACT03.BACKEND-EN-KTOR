package com.example.repositories

import com.example.database.Artists
import com.example.models.Artist
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant
import java.util.UUID

class ArtistRepository {

    fun create(artist: Artist): Artist = transaction {
        val id = Artists.insert {
            it[name] = artist.name
            it[genre] = artist.genre
            it[createdAt] = Instant.now()
            it[updatedAt] = Instant.now()
        }[Artists.id]

        artist.copy(
            id = id,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
    }

    fun findAll(): List<Artist> = transaction {
        Artists.selectAll().map { toArtist(it) }
    }

    fun findById(id: UUID): Artist? = transaction {
        Artists.select { Artists.id eq id }
            .map { toArtist(it) }
            .singleOrNull()
    }

    fun update(id: UUID, artist: Artist): Boolean = transaction {
        Artists.update({ Artists.id eq id }) {
            it[name] = artist.name
            it[genre] = artist.genre
            it[updatedAt] = Instant.now()
        } > 0
    }

    fun delete(id: UUID): Boolean = transaction {
        Artists.deleteWhere { Artists.id eq id } > 0
    }

    private fun toArtist(row: ResultRow): Artist = Artist(
        id = row[Artists.id],
        name = row[Artists.name],
        genre = row[Artists.genre],
        createdAt = row[Artists.createdAt],
        updatedAt = row[Artists.updatedAt]
    )
}