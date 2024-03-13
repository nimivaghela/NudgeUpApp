package com.app.collt.shared_data.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.collt.shared_data.repo.AuthRepo
import com.app.collt.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/*
* add all common api calls here,
* add logout API call here
* */
open class BaseViewModel(private val baseRepository: AuthRepo) : ViewModel() {


    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            /*baseRepository.result.collect {
                _stateFlow.tryEmit(it)
            }*/
        }
    }
}