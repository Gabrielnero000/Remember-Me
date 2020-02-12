package com.gabriel.rememberme

import android.app.Application
import com.gabriel.rememberme.data.database.RememberMeDatabase
import com.gabriel.rememberme.data.network.NetworkConnectionInterceptor
import com.gabriel.rememberme.data.network.RememberMeApi
import com.gabriel.rememberme.data.network.firebase.FirebaseSource
import com.gabriel.rememberme.data.preferences.PreferenceProvider
import com.gabriel.rememberme.data.repositories.MemoryRepository
import com.gabriel.rememberme.data.repositories.UserRepository
import com.gabriel.rememberme.ui.auth.viewmodel.AuthViewModelFactory
import com.gabriel.rememberme.ui.home.memory.MemoryViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class RememberMeApp : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@RememberMeApp))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { FirebaseSource() }
        bind() from singleton { RememberMeDatabase(instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { UserRepository(instance()) }
        bind() from singleton { MemoryRepository(instance(), instance(), instance(), instance()) }
        bind() from singleton { RememberMeApi(instance()) }
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { MemoryViewModelFactory(instance(), instance())
        }
    }

}