package com.sanghm2.newapp.repository

import com.sanghm2.newapp.api.RetrofitConfig
import com.sanghm2.newapp.database.ArticleDatabase
import com.sanghm2.newapp.model.Article

class NewRepository (
    val db : ArticleDatabase){
    suspend fun getBreakingNews(countryCode : String,pageNumber: Int) =
        RetrofitConfig.api.getBreakingNews(countryCode,pageNumber)
    suspend fun searchNews(searchQuery : String, pageNumber: Int) =
        RetrofitConfig.api.searchForNews(searchQuery,pageNumber)
    suspend fun getVnNew(domainString: String ,pageNumber: Int) =
        RetrofitConfig.api.getVnNew(domainString,pageNumber)

    suspend fun upsert(article: Article) = db.getArticle().upsert(article)

    fun getSaveNew() = db.getArticle().getAllArticle()

    suspend fun  deleteArticle(article: Article) = db.getArticle().deleteArticle(article)
}