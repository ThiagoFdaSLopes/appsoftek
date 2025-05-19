package com.grupo.appsoftek.data.model

data class QuoteResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Quote>
)

data class Quote(
    val id: String,
    val quote: String,
    val author: String,
    val tags: List<String>
)
