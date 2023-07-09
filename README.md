# SmartView

## Overview
This repository demonstrates how to interpret a JSON template in order to dynamically create views with data in Jetpack Compose. It supports a variety of components including text, button, line chart, scatter plot, table, and nested rows and columns.

## Features
- Interprets JSON templates and data to generate dynamic views in Jetpack Compose.
- Supports multiple types of components including text, button, line chart, scatter plot, and table.
- Supports nesting of rows and columns in the JSON template.
- Error handling to display error messages in case of failure in view generation.
  
## Usage
To use this project, you need to provide a JSON template that defines the structure of the view you want to generate, and JSON data that provides the content for these views.

Here's an example of a simple JSON template and data:

Template:
```json
{
  "type": "single",
  "dataTemplate": {
    "elements": [
      {
        "type": "text",
        "mapTo": "text1"
      }
    ]
  }
}
```

Here is the corresponding data:
```json
[
  {
    "text1": "This is the first text"
  }
]
```

## Setup
Clone the repository.
Open the project in Android Studio.
Run the project on an emulator or physical device.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details
