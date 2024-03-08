/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.insightglobal.demo.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.insightglobal.demo.data.local.database.Events
import com.insightglobal.demo.data.local.database.EventsDao
import com.insightglobal.demo.data.network.DiscoveryApi
import com.insightglobal.demo.data.network.EventsResponse
import com.insightglobal.demo.data.network.toDomain
import com.insightglobal.demo.domain.DomainEvent
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

interface EventsRepository {
    suspend fun getEventsByKeyword(keyword: String?): Result<List<DomainEvent>>
}

class RemoteEventsRepository @Inject constructor(
    private val eventsDao: EventsDao,
    private val discoveryApi: DiscoveryApi
) : EventsRepository {

    override suspend fun getEventsByKeyword(keyword: String?): Result<List<DomainEvent>> {
        return try {
            val response: Response<EventsResponse> = discoveryApi.getEventsByKeyword(keyword?:"")
            val events = response.body()?._embedded?.events ?: emptyList()
            val domainEvents = events.map { it.toDomain() }
            Result.success(domainEvents)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
