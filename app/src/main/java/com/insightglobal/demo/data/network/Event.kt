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
        image = images.firstOrNull { it.ratio == "16_9" || it.ratio == "4_3" }?.url,
        type = type,
        url = url
    ))
}