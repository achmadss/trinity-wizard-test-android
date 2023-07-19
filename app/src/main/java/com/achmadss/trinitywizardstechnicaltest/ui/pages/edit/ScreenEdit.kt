package com.achmadss.trinitywizardstechnicaltest.ui.pages.edit

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.achmadss.trinitywizardstechnicaltest.Routes
import com.achmadss.trinitywizardstechnicaltest.ui.pages.edit.ScreenEditListState.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ScreenEditUIState(
    var firstName: String = "-",
    var lastName: String = "-",
    var email: String = "-",
    var dob: String = "",
)

enum class ScreenEditListState {
    FIRST_NAME,
    LAST_NAME,
    EMAIL,
    DATE_OF_BIRTH
}

fun NavGraphBuilder.routeEdit(
    nav: NavHostController,
) {
    composable(
        route = "${Routes.EDIT}/{id}",
        arguments = listOf(
            navArgument("id") { type = NavType.StringType }
        )
    ) {
        val viewModel = hiltViewModel<EditViewModel>()
        val uiState by viewModel.uiState.observeAsState(
            initial = ScreenEditUIState()
        )
        val context = LocalContext.current
        LaunchedEffect(key1 = Unit, block = {
            viewModel.populateTextFields()
        })
        ScreenEdit(
            uiState = uiState,
            onSave = { firstName, lastName, email, dob ->
                viewModel.saveData(firstName, lastName, email, dob) {
                    val isSuccess = if (it) "success" else "failed"
                    Toast.makeText(context, isSuccess, Toast.LENGTH_SHORT).show()
                }
            },
            onCancel = {
                nav.popBackStack()
            }
        )
        BackHandler(true) {
            nav.popBackStack()
        }
    }
}

@Composable
fun ScreenEdit(
    uiState: ScreenEditUIState,
    onSave: (firstName: String, lastName: String, email: String, dob: String) -> Unit,
    onCancel: () -> Unit,
) {
    val states = remember {
        mutableStateListOf(
            FIRST_NAME,
            LAST_NAME,
            EMAIL,
            DATE_OF_BIRTH,
        )
    }
    val listState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dob by remember { mutableStateOf("") }

    firstName = uiState.firstName
    lastName = uiState.lastName
    email = uiState.email
    dob = uiState.dob

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(onClick = {
                    onCancel()
                }) {
                    Text(text = "Cancel")
                }
                TextButton(onClick = {
                    onSave(firstName, lastName, email, dob)
                }) {
                    Text(text = "Save")
                }
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(Color.Red)
                )
            }
        }
        itemsIndexed(states) { i, state ->
            if (i == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(18.dp)
                ) {
                    Text(
                        text = "Main Information",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
            if (i % 2 == 0 && i != 0) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(18.dp)
                ) {
                    Text(
                        text = "Sub Information",
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.3f),
                    text = when(state) {
                        FIRST_NAME -> "First Name"
                        LAST_NAME -> "Last Name"
                        EMAIL -> "Email"
                        DATE_OF_BIRTH -> "DOB"
                    },
                    fontSize = 20.sp,
                )
                TextField(
                    value = when(state) {
                        FIRST_NAME -> firstName
                        LAST_NAME -> lastName
                        EMAIL -> email
                        DATE_OF_BIRTH -> if (dob.isNotEmpty()) {
                            LocalDate.parse(dob, DateTimeFormatter.ofPattern("d/M/yyyy"))
                                .format(DateTimeFormatter.ofPattern("d MMMM yyyy"))
                        } else ""
                    },
                    onValueChange = {
                        when(state) {
                            FIRST_NAME -> firstName = it
                            LAST_NAME -> lastName = it
                            EMAIL -> email = it
                            DATE_OF_BIRTH -> { }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                            coroutineScope.launch {
                                listState.animateScrollToItem(i)
                            }
                        }
                    ),
                    trailingIcon = {
                        if (state == DATE_OF_BIRTH) {
                            Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar")
                        }
                    }
                )
            }
        }

    }
}