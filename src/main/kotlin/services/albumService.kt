package com.example.services

import com.example.models.Album
import com.example.repositories.AlbumRepository
import java.util.UUID

class AlbumService(private val repository: AlbumRepository) {
    fun createAlbum(album: Album): Album? = repository.create(album)
    fun getAllAlbums(): List<Album> = repository.findAll()
    fun getAlbumById(id: UUID): Album? = repository.findById(id)
    fun getAlbumsByArtist(artistId: UUID): List<Album> = repository.findByArtistId(artistId)
    fun updateAlbum(id: UUID, album: Album): Boolean = repository.update(id, album)
    fun deleteAlbum(id: UUID): Boolean = repository.delete(id)
}