package com.example.smartview.utils

import com.example.smartview.data.models.Component
import com.example.smartview.data.models.DynamicUIComponent
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import com.google.gson.reflect.TypeToken

data class KeyType(val key: String, val type: String, val parentKey: String? = null)
class JsonUtils {
    companion object {
        fun getKeyTypesFromJson(json: String): List<KeyType> {
            val gson = Gson()
            val jsonObject = gson.fromJson(json, JsonObject::class.java)

            return getKeyTypesFromJson(jsonObject)
        }
        //todo also add max and min's of key values for types e.g. length of string might affect the UI chosen
        fun getKeyTypesFromJson(
            jsonObject: JsonObject,
            parentKey: String? = null
        ): List<KeyType> {
            val keyTypes = mutableListOf<KeyType>()

            for ((key, value) in jsonObject.entrySet()) {
                val valueType = when (value) {
                    is JsonPrimitive -> getJsonPrimitiveType(value)
                    is JsonObject -> "JsonObject"
                    else -> "Unknown"
                }
                keyTypes.add(KeyType(key, valueType, parentKey))


                if (value is JsonObject) {
                    keyTypes.addAll(getKeyTypesFromJson(value))
                }
            }
            return keyTypes
        }

        fun formatKeyTypes(keyTypes: List<KeyType>): String {
            return keyTypes.joinToString(separator = "\n") {
                "${it.key}: ${it.type}${
                    if (it.parentKey != null) {
                        ", parentKey: $it.parentKey"
                    } else {
                        ""
                    }
                }"
            }
        }

        private fun getJsonPrimitiveType(jsonPrimitive: JsonPrimitive): String {
            return when {
                jsonPrimitive.isBoolean -> "Boolean"
                jsonPrimitive.isNumber -> "Number"
                jsonPrimitive.isString -> "String"
                else -> "Unknown"
            }
        }

        fun getTemplateAndDataFromResponse(rawTemplate:String, rawData:String ): DynamicUIComponent {
            val gson=Gson()
            val typeToken = object : TypeToken<List<Map<String, Any>>>() {}.type
            val component = gson.fromJson(rawTemplate, Component::class.java)
            val dataList: List<Map<String, Any>> = gson.fromJson(rawData, typeToken)
            return DynamicUIComponent(component,dataList)
        }
    }
}