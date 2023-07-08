package com.example.smartview.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartview.data.models.Component
import com.example.smartview.data.models.DynamicUIComponent

//Component: This class represents the main UI component specified in your template. Its type field specifies the type of the component (e.g., "list"), and its dataTemplate field specifies the template for the data items in this component.
@Composable
fun InterpretComponent(dynamicUIComponent: DynamicUIComponent) {
    InterpretComponent(dynamicUIComponent.template, dynamicUIComponent.data)
}

@Composable
fun InterpretComponent(component: Component, data: List<Map<String, Any>>) {
    when (component.type) {
        "list" -> {
            LazyColumn {
                items(data) { item ->
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        component.dataTemplate?.elements?.forEach { element ->
                            InterpretElement(element, item)
                        }
                    }
                }
            }
        }

        "single" -> {
            val item = data.firstOrNull()
            item?.let {
                component.dataTemplate?.elements?.firstOrNull()?.let { element ->
                    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
                        InterpretElement(element, item)
                    }
                }
            }
        }

        "null" -> {

        }
    }
}