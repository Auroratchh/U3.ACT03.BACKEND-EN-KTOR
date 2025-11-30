package com.example.services

import com.example.models.Track
import com.example.repositories.TrackRepository
import java.util.UUID

class TrackService(private val repository: TrackRepository) {
    fun createTrack(track: Track): Track? = repository.create(track)
    fun getAllTracks(): List<Track> = repository.findAll()
    fun getTrackById(id: UUID): Track? = repository.findById(id)
    fun getTracksByAlbum(albumId: UUID): List<Track> = repository.findByAlbumId(albumId)
    fun updateTrack(id: UUID, track: Track): Boolean = repository.update(id, track)
    fun deleteTrack(id: UUID): Boolean = repository.delete(id)
}