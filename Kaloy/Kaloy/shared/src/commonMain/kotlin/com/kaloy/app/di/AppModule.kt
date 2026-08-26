package com.kaloy.app.di

import com.kaloy.app.core.network.createHttpClient
import com.kaloy.app.data.repository.AuthRepository
import com.kaloy.app.data.repository.AuthRepositoryImpl
import com.kaloy.app.presentation.auth.register.RegisterViewModel
import org.koin.dsl.module

val appModule = module {
    single { createHttpClient() }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    factory { RegisterViewModel(get()) }
}
