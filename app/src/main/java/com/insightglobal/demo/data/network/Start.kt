package com.insightglobal.demo.data.network

data class Start(
    val dateTBA: Boolean,
    val dateTBD: Boolean,
    val localDate: String,
    val noSpecificTime: Boolean,
    val timeTBA: Boolean
)