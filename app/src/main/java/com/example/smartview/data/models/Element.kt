package com.example.smartview.data.models

data class Element(
    val type: String,
    val mapTo: String,
    val mapToList: List<String>?,
    val attributes: Attributes?,
    val elements: List<Element>?
)

data class Attributes(
    val text: String?,
    val headers: List<String>?
)