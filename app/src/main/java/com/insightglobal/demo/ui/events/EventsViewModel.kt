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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.insightglobal.demo.data.EventsRepository
import com.insightglobal.demo.domain.DomainEvent
import com.insightglobal.demo.ui.events.EventsUiState.Error
import com.insightglobal.demo.ui.events.EventsUiState.Loading
import com.insightglobal.demo.ui.events.EventsUiState.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(
    private val eventsRepository: EventsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<EventsUiState>(Loading)
    val uiState: StateFlow<EventsUiState> = _uiState

    var keyword: String by mutableStateOf("")
        private set

    init {
        viewModelScope.launch {
            eventsRepository
                .getEventsByKeyword("")
                .onSuccess {
                    _uiState.value = Success(it, "")
                }
                .onFailure {
                    _uiState.value = Error(it)
                }
        }

    }


    fun onEvent(event: EventsEvent) {
        when (event) {
            is EventsEvent.Search -> search(event.keyword)
        }

    }

    private fun search(keyword: String) {
        _uiState.value = Loading
        viewModelScope.launch {
            eventsRepository
                .getEventsByKeyword(keyword)
                .onSuccess {
                    _uiState.value = Success(it, keyword)
                }
                .onFailure {
                    _uiState.value = Error(it)
                }
        }
    }
}

sealed interface EventsUiState {
    data object Loading : EventsUiState
    data class Error(val throwable: Throwable) : EventsUiState
    data class Success(val data: List<DomainEvent>, var keyword: String = "") : EventsUiState
}

sealed interface EventsEvent {
    data class Search(val keyword: String) : EventsEvent
}

//We can use a data class to represent the state of the UI
//data class UiState(
//    val keyword: String = "",
//    val isLoading: Boolean = false,
//)
