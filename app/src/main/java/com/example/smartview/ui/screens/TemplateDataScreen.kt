package com.example.smartview.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.smartview.data.repository.ApiState
import kotlinx.coroutines.flow.Flow

@Composable
fun TemplateDataScreen(apiState: Flow<ApiState>, templates: Flow<List<String>>) {
    val screenState by apiState.collectAsState(initial = ApiState.IDLE)
    val templateString by templates.collectAsState(initial = "")
    when (screenState) {
        ApiState.IDLE -> Text(text = "No requests made")
        ApiState.GET_DATA_BUSY -> Text(text = "Getting data...")
        ApiState.GET_DATA_SUCCESS -> Text(text = "Getting data successful")
        ApiState.GET_DATA_FAILED -> Text(text = "Failed to get data")


        ApiState.SAMPLE_BUSY -> Text(text = "Analysing Data")
        ApiState.SAMPLE_SUCCESS -> Text(text = "Data Analysis Successful")
        ApiState.SAMPLE_FAILED -> Text(text = "Data Analysis Failed")


        ApiState.TEMPLATE_BUSY -> Text(text = "Generating UI")
        ApiState.TEMPLATE_SUCCESS -> {
            Box {
                Text(templateString.toString())
            }
        }

        ApiState.TEMPLATE_FAILED -> Text(text = "Generating UI Failed")
    }
}