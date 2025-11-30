package com.example.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import java.time.Instant
import java.util.UUID

@Serializable
data class Track(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    val title: String,
    @SerialName("duration")
    val duration: Int,
    @Serializable(with = UUIDSerializer::class)
    val albumId: UUID,
    @Serializable(with = InstantSerializer::class)
    val createdAt: Instant? = null,
    @Serializable(with = InstantSerializer::class)
    val updatedAt: Instant? = null
)