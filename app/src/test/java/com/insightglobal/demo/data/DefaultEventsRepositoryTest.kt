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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.insightglobal.demo.data.local.database.Events
import com.insightglobal.demo.data.local.database.EventsDao

/**
 * Unit tests for [DefaultEventsRepository].
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class DefaultEventsRepositoryTest {

    @Test
    fun eventss_newItemSaved_itemIsReturned() = runTest {
        val repository = DefaultEventsRepository(FakeEventsDao())

        repository.add("Repository")

        assertEquals(repository.eventss.first().size, 1)
    }

}

private class FakeEventsDao : EventsDao {

    private val data = mutableListOf<Events>()

    override fun getEventss(): Flow<List<Events>> = flow {
        emit(data)
    }

    override suspend fun insertEvents(item: Events) {
        data.add(0, item)
    }
}
