package com.example.routes

import com.example.models.Artist
import com.example.services.ArtistService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Route.artistRoutes(artistService: ArtistService) {
    route("/artistas") {
        post {
            val artist = call.receive<Artist>()
            val created = artistService.createArtist(artist)
            call.respond(HttpStatusCode.Created, created)
        }

        get {
            val artists = artistService.getAllArtists()
            call.respond(artists)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.let {
                try { UUID.fromString(it) } catch (e: Exception) { null }
            }
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "UUID inválido")
                return@get
            }
            val artist = artistService.getArtistById(id)
            if (artist == null) {
                call.respond(HttpStatusCode.NotFound, "Artista no encontrado")
            } else {
                call.respond(artist)
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]?.let {
                try { UUID.fromString(it) } catch (e: Exception) { null }
            }
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "UUID inválido")
                return@put
            }
            val artist = call.receive<Artist>()
            val updated = artistService.updateArtist(id, artist)
            if (updated) {
                call.respond(HttpStatusCode.OK, "Artista actualizado")
            } else {
                call.respond(HttpStatusCode.NotFound, "Artista no encontrado")
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]?.let {
                try { UUID.fromString(it) } catch (e: Exception) { null }
            }
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "UUID inválido")
                return@delete
            }
            val deleted = artistService.deleteArtist(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK, "Artista eliminado (con cascada)")
            } else {
                call.respond(HttpStatusCode.NotFound, "Artista no encontrado")
            }
        }
    }
}