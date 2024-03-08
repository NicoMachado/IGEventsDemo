package com.insightglobal.demo.domain

data class DomainEvent(
    val id: String,
    val name: String,
    val image: String?,
    val type: String,
    val url: String
)
