package com.example.smartview.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartview.utils.JsonUtils


@Composable
fun Table(headers: List<String>?, rows: List<Map<String, Any>>, keys: List<String>) {
    Surface(color = MaterialTheme.colorScheme.surface) {
        Column {
            // Render table headers
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
            ) {
                headers?.forEach { header ->
                    Text(
                        text = header,
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp, vertical = 12.dp)
                            .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)),
                        color = MaterialTheme.colorScheme.onPrimary,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            // Render table rows
            rows.forEachIndexed { index, row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (index % 2 == 0) MaterialTheme.colorScheme.onSurface.copy(
                                alpha = 0.05f
                            ) else Color.Transparent
                        )
                ) {
                    keys.forEach { key ->
                        Text(
                            text = row[key]?.toString() ?: "",
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp, vertical = 12.dp)
                                .border(
                                    1.dp,
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                                ),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDemoTable() {
    DemoTable()
}

@Composable
fun DemoTable() {
       val template = "{\n" +
        "  \"type\": \"list\",\n" +
        "  \"dataTemplate\": {\n" +
        "    \"elements\": [\n" +
        "      {\n" +
        "        \"type\": \"table\",\n" +
        "        \"mapTo\": \"salesData\",\n" +
        "        \"mapToList\": [\"product\", \"sales\", \"revenue\"],\n" +
        "        \"attributes\": {\n" +
        "          \"headers\": [\"Product\", \"Sales\", \"Revenue\"]\n" +
        "        }\n" +
        "      }\n" +
        "    ]\n" +
        "  }\n" +
        "}\n"

    val data = """
    [{
        "salesData": [
            {"product": "Product A", "sales": 100, "revenue": 500},
            {"product": "Product B", "sales": 200, "revenue": 1000},
            {"product": "Product C", "sales": 150, "revenue": 750}
        ]
    }]
""".trimIndent()

    InterpretComponent(JsonUtils.getTemplateAndDataFromResponse(template, data))
}