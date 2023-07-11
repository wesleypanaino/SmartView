package com.example.smartview.viewmodel

import android.util.Log
import android.webkit.URLUtil
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartview.data.repository.ApiRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ApiViewModel @Inject constructor(private val repository: ApiRepository) : ViewModel() {
    private val TAG = "ApiViewModel"
    var apiKey = mutableStateOf("")
    var endpoint = mutableStateOf("https://jsonplaceholder.typicode.com/posts")

    val updateFlow: Flow<JsonObject> = repository.updateFlow
    val dynamicUI = repository.dynamicUI
    val templates = repository.templates
    val apiState = repository.apiState

    private val _errorState = MutableStateFlow<ApiErrorState?>(null)
    val errorState: StateFlow<ApiErrorState?> = _errorState
    fun fetchData() {
        _errorState.value = null
        viewModelScope.launch {
            when {
                endpoint.value.isBlank() -> _errorState.value = ApiErrorState.InvalidEndpoint("Endpoint cannot be empty")
                !URLUtil.isValidUrl(endpoint.value) -> _errorState.value = ApiErrorState.InvalidEndpoint("Invalid URL for Endpoint")
                else -> {
                    _errorState.value = null
                    try {
                        repository.fetchData(endpoint.value, apiKey.value)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error fetching: $e")
                        _errorState.value = when(e) {
                            is IOException -> ApiErrorState.NetworkError
                            is HttpException -> ApiErrorState.ServerError
                            else -> ApiErrorState.UnknownError
                        }
                    }
                }
            }
        }
    }
    fun dismissError(){
        _errorState.value = null
    }
}
sealed class ApiErrorState {
    data class InvalidEndpoint(val error: String) : ApiErrorState()
    object NetworkError : ApiErrorState()
    object ServerError : ApiErrorState()
    object UnknownError : ApiErrorState()
}