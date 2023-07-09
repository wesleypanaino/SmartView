package com.example.smartview.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartview.data.models.Component
import com.example.smartview.data.models.DynamicUIComponent
import com.example.smartview.utils.JsonUtils

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
@Preview(showBackground = true)
@Composable
fun PreviewDemoList() {
    DemoList()
}

@Composable
fun DemoList() {
    val template = "{\n" +
        "  \"type\": \"list\",\n" +
        "  \"dataTemplate\": {\n" +
        "    \"elements\": [\n" +
        "      {\n" +
        "        \"type\": \"text\",\n" +
        "        \"mapTo\": \"id\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"type\": \"text\",\n" +
        "        \"mapTo\": \"title\"\n" +
        "      },\n" +
        "      {\n" +
        "        \"type\": \"button\",\n" +
        "        \"mapTo\": \"body\",\n" +
        "        \"attributes\": {\n" +
        "          \"text\": \"Details\"\n" +
        "        }\n" +
        "      }\n" +
        "    ]\n" +
        "  }\n" +
        "}"
    val data =
        "[{\"userId\":1,\"id\":1,\"title\":\"title 1\",\"body\":\"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\"},{\"userId\":2,\"id\":2,\"title\":\"title text 2\",\"body\":\"Body example\"}]"

    InterpretComponent(JsonUtils.getTemplateAndDataFromResponse(template, data))
}

@Preview(showBackground = true)
@Composable
fun PreviewDemoNested() {
    DemoNested()
}

@Composable
fun DemoNested() {
    val template = """{
  "type": "list",
  "dataTemplate": {
    "elements": [
      {
        "type": "row",
        "elements": [
          {
            "type": "text",
            "mapTo": "text1"
          },
          {
            "type": "text",
            "mapTo": "text2"
          }
        ]
      },
      {
        "type": "row",
        "elements": [
          {
            "type": "text",
            "mapTo": "text3"
          },
          {
            "type": "text",
            "mapTo": "text4"
          },
          {
            "type": "text",
            "mapTo": "text5"
          }
        ]
      }
    ]
  }
}
"""
    val data = """[
  {
    "text1": "This is the first text",
    "text2": "This is the second text",
    "text3": "This is the third text",
    "text4": "This is the fourth text",
    "text5": "This is the fifth text"
  }
]
"""
    InterpretComponent(JsonUtils.getTemplateAndDataFromResponse(template, data))
}
@Preview(showBackground = true)
@Composable
fun PreviewDemoTest() {
    DemoTest()
}

@Composable
fun DemoTest() {
    val template = """{
  "type": "list",
  "dataTemplate": {
    "elements": [
      {
        "type": "row",
        "elements": [
          {
            "type": "text",
            "mapTo": "title"
          },
          {
            "type": "text",
            "mapTo": "body"
          }
        ]
      }
    ]
  }
}"""
    val data =
        "[{\"userId\":1,\"id\":1,\"title\":\"title 1\",\"body\":\"quia et suscipit\\nsuscipit recusandae consequuntur expedita et cum\\nreprehenderit molestiae ut ut quas totam\\nnostrum rerum est autem sunt rem eveniet architecto\"},{\"userId\":2,\"id\":2,\"title\":\"title 2\",\"body\":\"Body example\"}]"

    InterpretComponent(JsonUtils.getTemplateAndDataFromResponse(template, data))
}
@Preview(showBackground = true)
@Composable
fun PreviewDemoSmart() {
    DemoSmart()
}

@Composable
fun DemoSmart() {
    val template = """{
  "type": "list",
  "dataTemplate": {
    "elements": [
      {
        "type": "text",
        "mapTo": "title"
      },
      {
        "type": "text",
        "mapTo": "description"
      },
      {
        "type": "lineChart",
        "mapTo": "dataPoints"
      }
    ]
  }
}
"""
    val data = """[
  {
    "title": "Title 1",
    "description": "This is a description for item 1.",
    "dataPoints": [42.5, 45.3, 44.8, 46.1, 45.9, 47.0, 46.8, 47.5]
  },
  {
    "title": "Title 2",
    "description": "This is a description for item 2.",
    "dataPoints": [50.3, 48.6, 51.2, 49.5, 50.8, 49.2, 50.5, 51.8]
  },
  {
    "title": "Title 3",
    "description": "This is a description for item 3.",
    "dataPoints": [40.1, 39.5, 41.2, 40.6, 39.9, 41.6, 40.0, 42.7]
  }
]
"""
    InterpretComponent(JsonUtils.getTemplateAndDataFromResponse(template, data))
}
@Preview(showBackground = true)
@Composable
fun DemoErrorList() {
    DemoError()
}

@Composable
fun DemoError() {
    val errorTemplate = """
{
  "type": "single",
  "dataTemplate": {
    "elements": [
      {
        "type": "error",
        "mapTo": "errorMessage"
      }
    ]
  }
}
"""

    val errorData = """
[
  {
    "errorMessage": "An error occurred while processing your request."
  }
]
"""
    InterpretComponent(JsonUtils.getTemplateAndDataFromResponse(errorTemplate, errorData))
}