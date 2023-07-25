package ru.netology.nework.entity

import ru.netology.nework.dto.EventType

data class EventTypeEmbeddable(
    val eventType: String,
) {
    fun toDto() = EventType.valueOf(eventType)

    companion object {
        fun fromDto(dto: EventType) = EventTypeEmbeddable(dto.name)
    }
}