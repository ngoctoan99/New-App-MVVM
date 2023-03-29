package com.sanghm2.newapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class NewViewModelProviderFactory(val app : Application,val newRepository: NewRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewViewModel(app,newRepository) as T
    }
}