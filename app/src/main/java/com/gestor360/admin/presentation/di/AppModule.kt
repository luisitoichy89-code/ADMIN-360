package com.gestor360.admin.presentation.di

import com.gestor360.admin.data.remote.SupabaseClientProvider
import com.gestor360.admin.data.repository.AuthRepositoryImpl
import com.gestor360.admin.domain.repository.AuthRepository
import org.koin.dsl.module

val appModule = module {

    single { SupabaseClientProvider.auth }

    single<AuthRepository> {
        AuthRepositoryImpl(
            supabase = get()
        )
    }
}
