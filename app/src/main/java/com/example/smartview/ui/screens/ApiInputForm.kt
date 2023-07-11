package com.example.smartview.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.smartview.viewmodel.ApiErrorState
import com.example.smartview.viewmodel.ApiViewModel

//todo allow for manual input of data , then also allow for generate similar data to example
//todo import from file
//todo show error states if API is wrong etc
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiInputForm(apiViewModel: ApiViewModel) {
    val errorMessage by apiViewModel.errorState.collectAsState()
    Column {
        Box {
            Column {
                OutlinedTextField(
                    value = apiViewModel.endpoint.value,
                    onValueChange = { apiViewModel.endpoint.value = it },
                    label = { Text("API Endpoint") }
                )
                OutlinedTextField(
                    value = apiViewModel.apiKey.value,
                    onValueChange = { apiViewModel.apiKey.value = it },
                    label = { Text("API Key") }
                )
                Button(
                    onClick = {
                        apiViewModel.fetchData()
                    }) {
                    Text("Submit")
                }
                errorMessage?.let {
                    val message = when (it) {
                        is ApiErrorState.InvalidEndpoint -> it.error
                        ApiErrorState.NetworkError -> "Network error"
                        ApiErrorState.ServerError -> "Server error"
                        ApiErrorState.UnknownError -> "Unknown error"
                    }
                    ShowErrorDialog(message) { apiViewModel.dismissError() }
                }
            }
        }
    }
}

@Composable
fun ShowErrorDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Error") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    )
}