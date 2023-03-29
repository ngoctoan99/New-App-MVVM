package com.sanghm2.newapp.model

import com.sanghm2.newapp.model.Article

data class NewResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)