package com.example.smartview.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.smartview.TAG
import com.example.smartview.data.repository.ApiState
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow

@Composable
fun SampleItem(apiState: Flow<ApiState>, updatedFlow: Flow<JsonObject>) {
    val updatedItem by updatedFlow.collectAsState(initial = JsonObject())
    val screenState by apiState.collectAsState(initial = ApiState.IDLE)
    when (screenState) {
        ApiState.IDLE -> Text(text = "No requests made")
        ApiState.GET_DATA_BUSY -> Text(text = "Getting data...")
        ApiState.GET_DATA_FAILED -> Text(text = "Failed to get data")
        else -> {
            Box {
                Column {
                    updatedItem.entrySet().forEach { (key, value) ->
                        Log.i(TAG, "$key: $value")
                        Text("$key: $value")
                    }
                }
            }
        }
    }
}