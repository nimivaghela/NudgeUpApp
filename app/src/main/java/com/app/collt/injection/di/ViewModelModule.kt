package com.app.collt.injection.di

import com.app.collt.shared_data.base.BaseViewModel
import com.app.collt.ui.auth.login.view_model.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

//    viewModel { BaseViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}