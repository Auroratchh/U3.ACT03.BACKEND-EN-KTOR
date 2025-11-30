package com.example.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.Instant

object Artists : Table("artistas") {
    val id = uuid("id").autoGenerate()
    val name = varchar("name", 100)
    val genre = varchar("genre", 50).nullable()
    val createdAt = timestamp("created_at").clientDefault { Instant.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }

    override val primaryKey = PrimaryKey(id)
}

object Albums : Table("albumes") {
    val id = uuid("id").autoGenerate()
    val title = varchar("title", 150)
    val releaseYear = integer("release_year").check { it greaterEq 1900 }
    val artistId = uuid("artist_id").references(Artists.id, onDelete = ReferenceOption.CASCADE)
    val createdAt = timestamp("created_at").clientDefault { Instant.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }

    override val primaryKey = PrimaryKey(id)
}

object Tracks : Table("tracks") {
    val id = uuid("id").autoGenerate()
    val title = varchar("title", 150)
    val duration = integer("duration").check { it greater 0 }
    val albumId = uuid("album_id").references(Albums.id, onDelete = ReferenceOption.CASCADE)
    val createdAt = timestamp("created_at").clientDefault { Instant.now() }
    val updatedAt = timestamp("updated_at").clientDefault { Instant.now() }

    override val primaryKey = PrimaryKey(id)
}

object DatabaseFactory {
    fun init() {
        val config = HikariConfig().apply {
            jdbcUrl = "jdbc:postgresql://localhost:5432/musicback_db"
            driverClassName = "org.postgresql.Driver"
            username = "postgres"
            password = "aurora28"
            maximumPoolSize = 10
        }

        val dataSource = HikariDataSource(config)
        Database.connect(dataSource)

        transaction {
            exec("CREATE EXTENSION IF NOT EXISTS \"uuid-ossp\";")

            SchemaUtils.create(Artists, Albums, Tracks)

            exec("CREATE INDEX IF NOT EXISTS idx_albumes_artist_id ON albumes(artist_id);")
            exec("CREATE INDEX IF NOT EXISTS idx_tracks_album_id ON tracks(album_id);")
        }
    }
}