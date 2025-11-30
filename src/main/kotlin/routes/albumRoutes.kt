package com.example.routes

import com.example.models.Album
import com.example.services.AlbumService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Route.albumRoutes(albumService: AlbumService) {
    route("/albumes") {
        post {
            val album = call.receive<Album>()
            val created = albumService.createAlbum(album)
            if (created == null) {
                call.respond(HttpStatusCode.BadRequest, "Artista no existe")
            } else {
                call.respond(HttpStatusCode.Created, created)
            }
        }

        get {
            val albums = albumService.getAllAlbums()
            call.respond(albums)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.let {
                try { UUID.fromString(it) } catch (e: Exception) { null }
            }
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "UUID inválido")
                return@get
            }
            val album = albumService.getAlbumById(id)
            if (album == null) {
                call.respond(HttpStatusCode.NotFound, "Álbum no encontrado")
            } else {
                call.respond(album)
            }
        }

        get("/artist/{artistId}") {
            val artistId = call.parameters["artistId"]?.let {
                try { UUID.fromString(it) } catch (e: Exception) { null }
            }
            if (artistId == null) {
                call.respond(HttpStatusCode.BadRequest, "UUID inválido")
                return@get
            }
            val albums = albumService.getAlbumsByArtist(artistId)
            call.respond(albums)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.let {
                try { UUID.fromString(it) } catch (e: Exception) { null }
            }
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "UUID inválido")
                return@put
            }
            val album = call.receive<Album>()
            val updated = albumService.updateAlbum(id, album)
            if (updated) {
                call.respond(HttpStatusCode.OK, "Álbum actualizado")
            } else {
                call.respond(HttpStatusCode.NotFound, "Álbum no encontrado o artista inválido")
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
            val deleted = albumService.deleteAlbum(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK, "Álbum eliminado (con cascada)")
            } else {
                call.respond(HttpStatusCode.NotFound, "Álbum no encontrado")
            }
        }
    }
}