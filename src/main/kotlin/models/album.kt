package com.example.models

import kotlinx.serialization.Serializable
import java.time.Instant
import java.util.UUID

@Serializable
data class Album(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    val title: String,
    val releaseYear: Int,
    @Serializable(with = UUIDSerializer::class)
    val artistId: UUID,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null
)