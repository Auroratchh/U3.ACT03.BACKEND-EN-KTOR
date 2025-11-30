package com.example.routes

import com.example.models.Track
import com.example.services.TrackService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Route.trackRoutes(trackService: TrackService) {
    route("/tracks") {
        post {
            val track = call.receive<Track>()
            val created = trackService.createTrack(track)
            if (created == null) {
                call.respond(HttpStatusCode.BadRequest, "Álbum no existe")
            } else {
                call.respond(HttpStatusCode.Created, created)
            }
        }

        get {
            val tracks = trackService.getAllTracks()
            call.respond(tracks)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.let {
                try { UUID.fromString(it) } catch (e: Exception) { null }
            }
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "UUID inválido")
                return@get
            }
            val track = trackService.getTrackById(id)
            if (track == null) {
                call.respond(HttpStatusCode.NotFound, "Track no encontrado")
            } else {
                call.respond(track)
            }
        }

        get("/album/{albumId}") {
            val albumId = call.parameters["albumId"]?.let {
                try { UUID.fromString(it) } catch (e: Exception) { null }
            }
            if (albumId == null) {
                call.respond(HttpStatusCode.BadRequest, "UUID inválido")
                return@get
            }
            val tracks = trackService.getTracksByAlbum(albumId)
            call.respond(tracks)
        }

        put("/{id}") {
            val id = call.parameters["id"]?.let {
                try { UUID.fromString(it) } catch (e: Exception) { null }
            }
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "UUID inválido")
                return@put
            }
            val track = call.receive<Track>()
            val updated = trackService.updateTrack(id, track)
            if (updated) {
                call.respond(HttpStatusCode.OK, "Track actualizado")
            } else {
                call.respond(HttpStatusCode.NotFound, "Track no encontrado o álbum inválido")
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
            val deleted = trackService.deleteTrack(id)
            if (deleted) {
                call.respond(HttpStatusCode.OK, "Track eliminado")
            } else {
                call.respond(HttpStatusCode.NotFound, "Track no encontrado")
            }
        }
    }
}