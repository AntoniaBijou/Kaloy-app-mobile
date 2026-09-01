package com.kaloy.app.di

import com.kaloy.app.core.network.createHttpClient
import com.kaloy.app.core.session.AuthSessionManager
import com.kaloy.app.data.repository.AuthRepository
import com.kaloy.app.data.repository.AuthRepositoryImpl
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val appModule = module {
    single { Settings() }
    single { AuthSessionManager(get()) }
    single { createHttpClient(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
}
