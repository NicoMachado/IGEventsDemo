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

package com.insightglobal.demo.ui.events


import com.insightglobal.demo.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import com.insightglobal.demo.data.EventsRepository
import com.insightglobal.demo.domain.DomainEvent
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class) // TODO: Remove when stable
class EventsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun uiState_initiallyLoading() = runTest(UnconfinedTestDispatcher()) {

        val expectedEvents = listOf(
            DomainEvent(
                id = "1",
                name = "Event 1",
                image = null,
                type = "Type 1",
                url = "url"
            )
        )
        val testResults = mutableListOf<EventsUiState>()
        val job = launch(UnconfinedTestDispatcher()) {
            val viewModel = EventsViewModel(MockEventsRepository())
            viewModel.uiState.toList( testResults )
        }

        assertEquals(2, testResults.size)
        assertEquals(EventsUiState.Loading, testResults.first() )
        assertEquals(EventsUiState.Success(expectedEvents, ""), testResults.last() )
        job.cancel()

    }


    @Test
    fun searchSuccessUpdatesUiState() = runTest {
        val mockRepository = MockEventsRepository()
        val viewModel = EventsViewModel(mockRepository)

        val keyword = "test"
        viewModel.onEvent(EventsEvent.Search(keyword))

        advanceUntilIdle()

        val expectedEvents = listOf(
            DomainEvent(
                id = "1",
                name = "Event 1",
                image = null,
                type = "Type 1",
                url = "url"
            )
        )
        assertEquals(EventsUiState.Success(expectedEvents, keyword), viewModel.uiState.first())
    }
}

private class MockEventsRepository : EventsRepository {
    private val data = mutableListOf<DomainEvent>(DomainEvent(
        id = "1",
        name = "Event 1",
        image = null,
        type = "Type 1",
        url = "url"
    ))

    override suspend fun getEventsByKeyword(keyword: String?): Result<List<DomainEvent>> {
        return Result.success(
            data
        )
    }
}
