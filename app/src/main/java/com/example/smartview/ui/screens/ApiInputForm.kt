package com.example.smartview.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.smartview.viewmodel.ApiViewModel

//todo allow for manual input of data , then also allow for generate similar data to example
//todo import from file
//todo show error states if API is wrong etc
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApiInputForm(apiViewModel: ApiViewModel) {
    var apiKey by remember { mutableStateOf("") }
    var endpoint by remember { mutableStateOf("https://jsonplaceholder.typicode.com/posts") }

    Column {
        Box {
            Column {
                OutlinedTextField(
                    value = endpoint,
                    onValueChange = { it: String -> endpoint = it },
                    label = { Text("API Endpoint") }
                )
                OutlinedTextField(
                    value = apiKey,
                    onValueChange = { it: String -> apiKey = it },
                    label = { Text("API Key") }
                )
                Button(onClick = {
                    apiViewModel.fetchData(endpoint, apiKey)
                }) {
                    Text("Submit")
                }
            }
        }
    }
}