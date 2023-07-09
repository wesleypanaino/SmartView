package com.example.smartview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.smartview.ui.screens.ApiInputForm
import com.example.smartview.ui.screens.DynamicUIScreen
import com.example.smartview.ui.screens.SampleItem
import com.example.smartview.ui.screens.TemplateDataScreen
import com.example.smartview.ui.theme.SmartViewTheme
import com.example.smartview.viewmodel.ApiViewModel
import dagger.hilt.android.AndroidEntryPoint

val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val apiViewModel: ApiViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SmartViewTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val tabList = listOf("Get Data", "JSON Data", "Templates", "Interpretation")
                    var selectedTabIndex by remember { mutableStateOf(0) }
                    Column {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.primary,
                            //  elevation = 8.dp
                        ) {
                            TabRow(
                                selectedTabIndex = selectedTabIndex,
                                modifier = Modifier.background(color = MaterialTheme.colorScheme.primary),
                                contentColor = Color.White
                            ) {
                                tabList.forEachIndexed { index, title ->
                                    Tab(
                                        text = { Text(title) },
                                        selected = selectedTabIndex == index,
                                        onClick = { selectedTabIndex = index }
                                    )
                                }
                            }
                        }
                        when (selectedTabIndex) {
                            0 -> ApiInputForm(apiViewModel)
                            1 -> SampleItem(apiViewModel.apiState, apiViewModel.updateFlow)

                            2 -> TemplateDataScreen(apiViewModel.apiState, apiViewModel.templates)

                            3 -> DynamicUIScreen(apiViewModel.apiState, apiViewModel.dynamicUI)
                        }
                    }

                }
            }
        }
    }
}