package com.app.collt.injection.di

import com.app.collt.shared_data.repo.AuthRepo
import com.app.collt.shared_data.repo.AuthRepository
import org.koin.dsl.module


val repositoryModule = module {
    single<AuthRepo> { AuthRepository(get(), get()) }
}