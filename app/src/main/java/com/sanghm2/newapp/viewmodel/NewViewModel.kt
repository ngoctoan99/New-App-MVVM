package com.sanghm2.newapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sanghm2.newapp.NewApplication
import com.sanghm2.newapp.repository.NewRepository
import com.sanghm2.newapp.model.Article
import com.sanghm2.newapp.model.NewResponse
import com.sanghm2.newapp.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewViewModel (
    app: Application,
    val newRepository: NewRepository
): AndroidViewModel(app){

    val breakingNew : MutableLiveData<Resource<NewResponse>> = MutableLiveData()
    var breakingNewPage = 1
    var breakingNewResponse : NewResponse ? = null


    val searchNew : MutableLiveData<Resource<NewResponse>> = MutableLiveData()
    var searchNewPage = 1
    var searchNewResponse : NewResponse ? = null

    init{
        getBreakingNews("us")
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
//        breakingNew.postValue(Resource.Loading())
//        val response = newRepository.getBreakingNews(countryCode,breakingNewPage)
//        breakingNew.postValue(handleBreakingNewsResponse(response))
        safeBreakingNewCall(countryCode)
    }

    fun searchNew(searchQuery: String) = viewModelScope.launch {
        searchNew.postValue(Resource.Loading())
        val response = newRepository.searchNews(searchQuery,searchNewPage)
        searchNew.postValue(handleSearchNewsResponse(response))
    }

    private  fun handleBreakingNewsResponse(response: Response<NewResponse>): Resource<NewResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private  fun handleSearchNewsResponse(response: Response<NewResponse>): Resource<NewResponse>{
        if(response.isSuccessful){
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveArticle(article : Article) = viewModelScope.launch {
        newRepository.upsert(article)
    }

    fun getSaveNews() = newRepository.getSaveNew()
    fun deleteArticle(article: Article) = viewModelScope.launch {
        newRepository.deleteArticle(article)
    }
    private suspend fun safeBreakingNewCall(countryCode: String){
        breakingNew.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()){
                val response = newRepository.getBreakingNews(countryCode,breakingNewPage)
                breakingNew.postValue(handleBreakingNewsResponse(response))
            }else {
                breakingNew.postValue(Resource.Error("No internet connection"))
            }
        }catch (t : Throwable){
            when(t){
                is IOException -> breakingNew.postValue(Resource.Error("Network Failure"))
                else -> breakingNew.postValue(Resource.Error("Conversion Error"))
            }
        }
    }
    private fun hasInternetConnection(): Boolean{
        val connectivityManager = getApplication<NewApplication>()
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE-> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}