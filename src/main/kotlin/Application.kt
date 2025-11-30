package com.example

import com.example.database.DatabaseFactory
import com.example.repositories.AlbumRepository
import com.example.repositories.ArtistRepository
import com.example.repositories.TrackRepository
import com.example.routes.albumRoutes
import com.example.routes.artistRoutes
import com.example.routes.trackRoutes
import com.example.services.AlbumService
import com.example.services.ArtistService
import com.example.services.TrackService
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {

    DatabaseFactory.init()

    val artistRepository = ArtistRepository()
    val albumRepository = AlbumRepository()
    val trackRepository = TrackRepository()

    val artistService = ArtistService(artistRepository)
    val albumService = AlbumService(albumRepository)
    val trackService = TrackService(trackRepository)

    install(ContentNegotiation) {
        json()
    }

    routing {
        get("/") {
            call.respondText("Backend ktor")
        }
        route("/api") {
            artistRoutes(artistService)
            albumRoutes(albumService)
            trackRoutes(trackService)
        }
    }
}