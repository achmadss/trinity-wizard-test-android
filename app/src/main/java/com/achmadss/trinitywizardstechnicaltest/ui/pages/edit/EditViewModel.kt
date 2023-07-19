package com.achmadss.trinitywizardstechnicaltest.ui.pages.edit

import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadss.data.repository.AppDataRepository
import com.achmadss.domain.entity.Contacts
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class EditViewModel @Inject constructor(
    private val appDataRepository: AppDataRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private var _uiState = MutableLiveData<ScreenEditUIState>()
    val uiState get() = _uiState

    fun populateTextFields() = viewModelScope.launch {
        val id = savedStateHandle.get<String>("id")
        id?.let {
            appDataRepository.getDataById(it).collect { data ->
                _uiState.value = ScreenEditUIState(
                    firstName = data.firstName,
                    lastName = data.lastName,
                    email = data.email ?: "",
                    dob = data.dob ?: "",
                )
            }
        }
    }

    fun saveData(
        firstName: String,
        lastName: String,
        email: String,
        dob: String,
        callback: (Boolean) -> Unit,
    ) = viewModelScope.launch {
        appDataRepository.updateData(Contacts(
            savedStateHandle.get<String>("id") ?: "", firstName, lastName, email, dob
        )).collect {
            callback(it)
        }
    }

}