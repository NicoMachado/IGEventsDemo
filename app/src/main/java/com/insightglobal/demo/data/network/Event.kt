package com.insightglobal.demo.data.network

import com.insightglobal.demo.domain.DomainEvent

data class Event(
    val dates: Dates,
    val id: String,
    val images: List<ImageX>,
    val locale: String,
    val name: String,
    val test: Boolean,
    val type: String,
    val url: String
)

fun Event.toDomain() : DomainEvent {
    return (DomainEvent(
        id = id,
        name = name,
        image = images.firstOrNull()?.url,
        type = type,
        url = url
    ))
}