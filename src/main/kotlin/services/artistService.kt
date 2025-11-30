package com.example.services

import com.example.models.Artist
import com.example.repositories.ArtistRepository
import java.util.UUID

class ArtistService(private val repository: ArtistRepository) {
    fun createArtist(artist: Artist): Artist = repository.create(artist)
    fun getAllArtists(): List<Artist> = repository.findAll()
    fun getArtistById(id: UUID): Artist? = repository.findById(id)
    fun updateArtist(id: UUID, artist: Artist): Boolean = repository.update(id, artist)
    fun deleteArtist(id: UUID): Boolean = repository.delete(id)
}