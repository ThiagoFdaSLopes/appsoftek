package com.grupo.appsoftek.data.model

data class QuoteResponse(
    val next: String?,
    val previous: String?,
    val results: List<Quote>
)

data class Quote(
    val quote: String,
    val author: String?,
    val likes: Int,
    val tags: List<String>,
    val pk: Int,
    val image: String?,
    val language: String
)