package com.insightglobal.demo.data.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DiscoveryApi {
    @GET("events.json?apikey=DW0E98NrxUIfDDtNN7ijruVSm60ryFLX")
    suspend fun getEventsByKeyword(@Query("keyword") keyword: String?): Response<EventsResponse>
}
