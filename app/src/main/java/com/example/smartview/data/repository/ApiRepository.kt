package com.example.smartview.data.repository

import android.util.Log
import com.example.smartview.data.models.Component
import com.example.smartview.data.models.DynamicUIComponent
import com.example.smartview.data.services.ApiService
import com.example.smartview.data.services.ChatCompletion
import com.example.smartview.data.services.OpenAiService
import com.example.smartview.utils.JsonUtils
import com.example.smartview.utils.OpenAiUtils
import com.example.smartview.utils.OpenAiUtils.Companion.getSystemMessage
import com.example.smartview.utils.OpenAiUtils.Companion.getUserMessage
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

enum class ApiState {
    IDLE,
    GET_DATA_BUSY,
    GET_DATA_SUCCESS,
    GET_DATA_FAILED,
    SAMPLE_BUSY,
    SAMPLE_SUCCESS,
    SAMPLE_FAILED,
    TEMPLATE_BUSY,
    TEMPLATE_SUCCESS,
    TEMPLATE_FAILED,
}

class ApiRepository(private val apiService: ApiService, private val openAiService: OpenAiService) {
    private val TAG = "ApiRepository"

    private val _updateFlow = MutableStateFlow(JsonObject())
    val updateFlow: Flow<JsonObject> = _updateFlow.asStateFlow()

    private val _templates: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val templates: Flow<List<String>> = _templates.asStateFlow()


    private val _dynamicUI = MutableStateFlow(
        DynamicUIComponent(
            Component("null", null),
            emptyList()
        )
    )
    val dynamicUI: Flow<DynamicUIComponent> = _dynamicUI.asStateFlow()

    private val _apiState = MutableStateFlow(ApiState.IDLE)
    val apiState: Flow<ApiState> = _apiState.asStateFlow()

    //todo allow user to select relevant data
    suspend fun fetchData(endpoint: String, apiKey: String?) {
        return withContext(Dispatchers.IO) {
            _apiState.value = ApiState.GET_DATA_BUSY
            Log.i(TAG, "fetchData from: $endpoint with apikey: $apiKey")
            val response = apiService.fetchData(endpoint, apiKey)
            if (response.isSuccessful) {
                _apiState.value = ApiState.GET_DATA_SUCCESS
                Log.i(TAG, "response: $response")

                val fullHeader = response.raw().headers
                val contentType = fullHeader.values("content-type")
                val contentLength = fullHeader.values("content-length")
                Log.i(TAG, "contentType: $contentType")
                Log.i(TAG, "contentLength: $contentLength")

                _apiState.value = ApiState.SAMPLE_BUSY
                val sample = getSample(response)
                if (sample.isNullOrBlank()) {
                    _apiState.value = ApiState.SAMPLE_FAILED
                    Log.e(TAG, "Error getSample $response, $sample")
                    throw Exception("Error getSample")
                }
                val apiKeyTypes = handleJSON(sample)
                if (apiKeyTypes.isNullOrBlank()) {
                    _apiState.value = ApiState.SAMPLE_FAILED
                    Log.e(TAG, "Error handleJSON $sample, $apiKeyTypes")
                    throw Exception("Error handleJSON")
                }
                _apiState.value = ApiState.SAMPLE_SUCCESS
                _apiState.value = ApiState.TEMPLATE_BUSY
                val template = getTemplate(apiKeyTypes)
                Log.d(TAG, "template printout: $template")

                _apiState.value = ApiState.TEMPLATE_SUCCESS
                _dynamicUI.value = JsonUtils.getTemplateAndDataFromResponse(template, sample)

            } else {
                _apiState.value = ApiState.GET_DATA_FAILED
                throw Exception("Error fetching data")
            }
        }
    }

    private suspend fun getSample(response: Response<ResponseBody>): String? {
        return withContext(Dispatchers.IO) {
            val contentType = response.raw().headers.values("content-type").firstOrNull()
            Log.i(TAG, "getSample")
            val contentTypeValue = contentType!!.split(";")[0].trim()
            Log.i(TAG, "contentTypeValue: $contentTypeValue")

            when (contentTypeValue) {
                "application/json" -> {
                    // Handle JSON content type
                    val jsonString = response.body()?.string()
                    Log.i(TAG, "jsonString: ${jsonString?.length}")
                    jsonString
                }

                else -> {
                    // Handle unrecognized or unsupported content type
                    Log.e(TAG, "Unrecognized or unsupported content type $contentType")
                    null
                }
            }
        }
    }
    //todo send actual data item if within size limits else just send keys
    //todo better error handling instead of null use result
    private suspend fun handleJSON(jsonString: String?): String? {
        return withContext(Dispatchers.IO) {
            if (jsonString != null) {
                try {
                    val jsonElement = Gson().fromJson(jsonString, JsonElement::class.java)
                    if (jsonElement.isJsonArray) {
                        val jsonArray = jsonElement.asJsonArray
                        val jsonItem = jsonArray.first()
                        //Assume first item dictates rest of array type
                        Log.d(TAG, "JSON Array size: ${jsonArray.size()}\n Type: ")
                        if (jsonItem.isJsonObject) {
                            Log.d(TAG, "isJsonObject")
                            // Handle object element
                            _updateFlow.value = jsonItem.asJsonObject

                            val listKeyTypes = JsonUtils.getKeyTypesFromJson(jsonItem.asJsonObject)
                            Log.i(TAG, "getKeyTypesFromJson:\n$listKeyTypes")
                            val apiKeyTypes = JsonUtils.formatKeyTypes(listKeyTypes)
                            Log.i(TAG, "apiKeyTypes:\n$apiKeyTypes")
                            return@withContext apiKeyTypes

                        } else if (jsonItem.isJsonArray) {
                            Log.e(TAG, "Not supported yet")
                            return@withContext null
                        } else  {
                            Log.e(TAG, "Not supported yet")
                            return@withContext null
                        }
                    } else {
                        // Handle other types of JSON
                        Log.e(TAG, "JSON Unknown type")
                        Log.e(TAG, "Not supported yet")
                        return@withContext null
                    }
                } catch (e: Exception) {
                    // Handle JSON parsing error
                    Log.e(TAG, "Error parsing json: $e")
                    return@withContext null
                }
            } else {
                return@withContext null
            }
        }
    }

    private suspend fun getTemplate(content: String): String {
        return withContext(Dispatchers.IO) {
            val chatCompletion = ChatCompletion(
                model = "gpt-3.5-turbo",
                messages = listOf(
                    getSystemMessage(),
                    getUserMessage("ArrayOf{$content}")
                ),
                max_tokens = OpenAiUtils.getMaxTokens()
            )
            val call = openAiService.createChatCompletion(
                chatCompletion
            )
            val choice = call.choices.first()

            _templates.value = call.choices.map { it.message.content }
            Log.d(TAG, "onResponse $call")
            choice.message.content
        }
    }
}