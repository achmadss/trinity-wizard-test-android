package com.achmadss.trinitywizardstechnicaltest.ui.pages.main

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.achmadss.data.repository.AppDataRepository
import com.achmadss.data.util.readAssetsFile
import com.achmadss.domain.entity.Contacts
import com.achmadss.domain.model.ContactModel
import com.achmadss.domain.model.toContacts
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val appDataRepository: AppDataRepository
): ViewModel() {

    private var _uiState = MutableLiveData<ScreenMainUIState>()
    val uiState get() = _uiState

    fun initData(context: Context) = viewModelScope.launch {
        appDataRepository.getAllData().collect { data ->
            if (data.isEmpty()) {
                try {
                    val gson = Gson()
                    val jsonData = context.assets.readAssetsFile("data.json")
                    val list = gson.fromJson(jsonData, Array<ContactModel>::class.java).map { it.toContacts() }
                    appDataRepository.insertNewData(list).collect {
                        getAndPopulateData()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else {
                getAndPopulateData()
            }
        }
    }

    fun forceRefreshData(context: Context) = viewModelScope.launch {
        try {
            val gson = Gson()
            val jsonData = context.assets.readAssetsFile("data.json")
            val list = gson.fromJson(jsonData, Array<ContactModel>::class.java).map { it.toContacts() }
            appDataRepository.insertNewData(list).collect {
                getAndPopulateData()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getAndPopulateData() = viewModelScope.launch {
        appDataRepository.getAllData().collect {
            _uiState.postValue(ScreenMainUIState(
                data = it, isLoading = false
            ))
        }
    }

}