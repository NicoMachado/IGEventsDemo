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

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.insightglobal.demo.R
import com.insightglobal.demo.domain.DomainEvent
import com.insightglobal.demo.ui.theme.MyApplicationTheme
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(modifier: Modifier = Modifier, viewModel: EventsViewModel = hiltViewModel()) {
    val items by viewModel.uiState.collectAsStateWithLifecycle()
    var showToast by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    if (showToast) {
        Toast.makeText(
             context,
            "Hire me! And your ticket will be added to cart",
            Toast.LENGTH_LONG
        ).show()
        showToast = false
    }

    Scaffold(modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Text(
                        text = "Simple TM Events List",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                })
        }) { padding ->
        EventsContent(
            items = items,
            modifier = modifier.padding(padding),
            onSearch = { viewModel.onEvent(EventsEvent.Search(it)) },
            onSwipe = {
                Log.d("EventsScreen", "Swipe action")
                showToast = true }
        )

    }
}

@Composable
fun EventsContent(
    items: EventsUiState, modifier: Modifier,
    onSearch: (String) -> Unit,
    onSwipe: () -> Unit
) {
    if (items is EventsUiState.Loading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
                Text(
                    "Loading...",
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    } else {
        EventsScreen(
            events = (items as EventsUiState.Success).data,
            search = items.keyword,
            onSearch = { onSearch(it) },
            onSwipe = { onSwipe()
                Log.d("EventsScreen", "onSwipe") },
            modifier = modifier
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EventsScreen(
    events: List<DomainEvent>,
    search: String = "",
    onSearch: (String) -> Unit = {},
    onSwipe: () -> Unit ,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val cartAction = SwipeAction(
        icon = rememberVectorPainter(Icons.Filled.ShoppingCart),
        background = Color.Green,
        onSwipe = {
            onSwipe()
        }
    )

    Column(modifier.fillMaxWidth()) {

        var keyword by remember { mutableStateOf(search) }
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
                .semantics {
                    contentDescription = "Search"
                },
            value = keyword,
            singleLine = true,
            onValueChange = { keyword = it },
            trailingIcon = {
                IconButton(onClick = {
                    keyword = ""
                    onSearch(keyword)
                }) {
                    Icon(Icons.Filled.Clear, contentDescription = "Clear")
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearch(keyword) }),
        )

        if (events.isEmpty()) {
            NoEventsComposable()
        }

        if (events.isNotEmpty()) {
            val lazyListState = rememberLazyListState()

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),

                state = lazyListState
            ) {

                item {
                    Text(
                        "Please, choose your Event!",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                items(
                    items = events,
                    key = { it.id }) {

                    SwipeableActionsBox(
                        startActions = listOf(cartAction),
                    ) {

                        Card(
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { Log.d("EventsScreen", "Event clicked: $it") }
                                .heightIn(min = 90.dp)
                                .padding(vertical = 8.dp, horizontal = 12.dp)
                        ) {
                            Row() {
                                AsyncImage(
                                    model = it.image,
                                    contentDescription = it.name,
                                    contentScale = ContentScale.Crop,
                                    onState = {
                                        if (it is AsyncImagePainter.State.Error) {
                                            it.result.throwable.printStackTrace()
                                        }
                                    },
                                    modifier = Modifier
                                        .size(90.dp, 90.dp)
                                        .border(
                                            1.dp,
                                            MaterialTheme.colorScheme.primary,
                                            shape = RoundedCornerShape(4.dp)
                                        )
                                        .clip(RoundedCornerShape(4.dp))
                                )
                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 8.dp, vertical = 12.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(it.name, style = MaterialTheme.typography.titleMedium)
                                    Text(it.type, style = MaterialTheme.typography.bodyMedium)
                                }
                            }

                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NoEventsComposable() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.padding(16.dp)

        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search_off),
                    contentDescription = "Not Found"
                )
                Text(
                    text = "No events found!\nPlease try again with another keyword",
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    MyApplicationTheme {
        EventsScreen(events = emptyList(), onSearch = {}, onSwipe = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        EventsScreen(events = emptyList(), onSearch = {}, onSwipe = {})
    }
}
