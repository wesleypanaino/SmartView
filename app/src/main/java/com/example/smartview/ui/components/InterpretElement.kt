package com.example.smartview.ui.components

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartview.TAG
import com.example.smartview.data.models.Element

@Composable
fun InterpretElement(element: Element, data: Map<String, Any>) {
    val context = LocalContext.current
    when (element.type) {
        "text" -> {
            Text(modifier = Modifier.padding(4.dp), text = data[element.mapTo]?.toString() ?: "")
        }

        "button" -> {
            Button(
                onClick = {
                    Toast.makeText(
                        context,
                        data[element.mapTo]?.toString() ?: "",
                        Toast.LENGTH_LONG
                    ).show()
                },
                modifier = Modifier.background(color = MaterialTheme.colorScheme.primaryContainer)

            ) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = element.attributes?.text ?: "",
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        "row" -> {
            Row {
                element.elements?.forEach { childElement ->
                    InterpretElement(childElement, data)
                }
            }
        }

        "column" -> {
            Column {
                element.elements?.forEach { childElement ->
                    InterpretElement(childElement, data)
                }
            }
        }

        //todo update so that can take in x y co-ords instead of just list
        "lineChart" -> {
            Log.d(TAG, "lineChart ${data[element.mapTo]}")
            val floatData = (data[element.mapTo] as List<Double>).map { it.toFloat() }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.5f)
            ) {
                LineChart(data = floatData, modifier = Modifier.fillMaxSize())
            }
        }

        "scatterPlot" -> {
            val scatterData = (data[element.mapTo] as List<Map<String, Double>>)
                .map { map ->
                    val keys = map.keys.toList()
                    val x = map[keys[0]]!!.toFloat()
                    val y = map[keys[1]]!!.toFloat()
                    x to y
                }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = LocalConfiguration.current.screenHeightDp.dp * 0.5f)
            ) {
                ScatterPlot(data = scatterData, modifier = Modifier.fillMaxSize())
            }
        }

        "table" -> {
            element.mapToList?.let { keys ->
                Table(
                    headers = element.attributes?.headers,
                    rows = data[element.mapTo] as List<Map<String, Any>>,
                    keys = keys
                )
            }
        }

        "error" -> {
            Text(
                text = data[element.mapTo]?.toString() ?: "An error occurred",
                color = Color.Red,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
        }
    }
}