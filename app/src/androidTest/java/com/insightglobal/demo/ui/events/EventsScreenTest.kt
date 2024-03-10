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

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.insightglobal.demo.data.di.FAKE_EVENTS
import com.insightglobal.demo.domain.DomainEvent
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * UI tests for [EventsScreen].
 */
@RunWith(AndroidJUnit4::class)
class EventsScreenTest {

    @get:Rule
    val composeTestRule = createEmptyComposeRule()

//    @get:Rule
//    val composeTestRule = createAndroidComposeRule<ComponentActivity>()
    private lateinit var scenario: ActivityScenario<ComponentActivity>

    @Before
    fun setup() {
        scenario = ActivityScenario.launch(ComponentActivity::class.java)
//        composeTestRule.setContent {
//            EventsScreen(FAKE_DATA.getOrThrow(), onSearch = {})
//        }
    }

    @After
    fun tearDown() {
        scenario.close()
    }

    @Test
    fun firstItem_exists() {
        initComposable(FAKE_DATA.getOrThrow())
        composeTestRule.onNodeWithText(FAKE_DATA.getOrThrow().first().name).assertExists().performClick()
    }

    @Test
    fun testSearchInMainScreen() {
        initComposable(FAKE_DATA.getOrThrow())
        composeTestRule.onNodeWithContentDescription("Search", ignoreCase = true).assertExists()
    }

    @Test
    fun testEmptyEventsScreen() {
        initComposable(emptyList())

        composeTestRule.onNodeWithContentDescription("Not Found", ignoreCase = true).assertExists()
    }

    private fun initComposable(events: List<DomainEvent>) {
        scenario.onActivity { activity ->
            activity.setContent {
                EventsScreen()
            }
        }
    }
}

private val FAKE_DATA = Result.success(
    FAKE_EVENTS
)