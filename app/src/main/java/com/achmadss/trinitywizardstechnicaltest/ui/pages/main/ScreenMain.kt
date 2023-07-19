package com.achmadss.trinitywizardstechnicaltest.ui.pages.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.achmadss.domain.entity.Contacts
import com.achmadss.trinitywizardstechnicaltest.Routes
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

data class ScreenMainUIState(
    var data: List<Contacts> = listOf(),
    var isLoading: Boolean = true,
)

fun NavGraphBuilder.routeMain(
    nav: NavHostController,
) {
    composable(
        route = Routes.MAIN
    ) {
        val viewModel = hiltViewModel<MainViewModel>()
        val uiState by viewModel.uiState.observeAsState(
            initial = ScreenMainUIState()
        )
        val context = LocalContext.current
        LaunchedEffect(key1 = Unit, block = {
            viewModel.initData(context)
        })
        ScreenMain(
            uiState = uiState,
            onCardClick = {
                nav.navigate("${Routes.EDIT}/$it")
            },
            onRefresh = {
                viewModel.forceRefreshData(context)
            }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScreenMain(
    uiState: ScreenMainUIState,
    onCardClick: (String) -> Unit,
    onRefresh: () -> Unit,
) {
    var isRefreshing by remember { mutableStateOf(false) }
    isRefreshing = uiState.isLoading
    val coroutineScope = rememberCoroutineScope()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            isRefreshing = true
            coroutineScope.launch {
                onRefresh()
            }
        },
    )
    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = 16.dp, start = 16.dp, end = 16.dp
                    ),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                Text(text = "Contacts", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Icon(imageVector = Icons.Default.Add, contentDescription = "add")
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .pullRefresh(
                        state = pullRefreshState,
                        enabled = true
                    ),
            ) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(
                        bottom = 16.dp
                    ),
                ) {
                    items(uiState.data) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 8.dp)
                                .background(Color.White)
                                .border(BorderStroke(1.dp, Color.LightGray))
                                .clickable { onCardClick(it.id) },
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(Color.Red)
                                )
                                Text(
                                    text = "${it.firstName} ${it.lastName}",
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
            
        }

    }

}

@Composable
fun DataDetail(
    title: String,
    value: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(text = title, fontSize = 8.sp)
        Text(text = value, fontSize = 16.sp)
    }
}
