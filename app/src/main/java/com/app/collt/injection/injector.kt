package com.app.collt.injection

import com.app.collt.injection.di.dispatcherModule
import com.app.collt.injection.di.networkModule
import com.app.collt.injection.di.repositoryModule
import com.app.collt.injection.di.sharedPreferenceModule
import com.app.collt.injection.di.viewModelModule


val appModules =
    repositoryModule + viewModelModule + networkModule + sharedPreferenceModule + dispatcherModule


