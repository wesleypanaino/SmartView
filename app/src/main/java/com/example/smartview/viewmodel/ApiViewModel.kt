package com.example.smartview.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartview.data.repository.ApiRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {
    private val TAG = "ApiViewModel"

    val updateFlow: Flow<JsonObject> = repository.updateFlow
    val dynamicUI = repository.dynamicUI
    val templates = repository.templates
    val apiState = repository.apiState
    fun fetchData(endpoint: String, apiKey: String?) {
        viewModelScope.launch {
            try {
              repository.fetchData(endpoint, apiKey)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching: $e")
            }
        }
    }
}