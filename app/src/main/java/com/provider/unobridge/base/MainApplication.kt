package com.provider.unobridge.base

import android.app.Activity
import android.app.Application
import com.provider.unobridge.dataSources.remote.RemoteDataSource
import com.provider.unobridge.dataSources.remote.RemoteDataSourceImp
import com.provider.unobridge.network.core.APIService
import com.provider.unobridge.providers.firebasePhoneAuth.FirebaseAuthPhone
import com.provider.unobridge.providers.firebasePhoneAuth.FirebaseAuthPhoneImpl
import com.provider.unobridge.providers.payment.PaymentProvider
import com.provider.unobridge.providers.payment.PaymentProviderImpl
import com.provider.unobridge.repo.RemoteRepository
import com.provider.unobridge.repo.RemoteRepositoryImpl
import com.provider.unobridge.ui.activities.MainActivity
import com.provider.unobridge.ui.fragments.account.viewModel.AccountViewModelFactory
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class MainApplication : Application(), KodeinAware {

    companion object {
        lateinit var instance: MainApplication
        fun get(): MainApplication = instance


    }


    private var mCurrentActivity: Activity? = null

    fun getCurrentActivity(): Activity? {
        return mCurrentActivity
    }

    override val kodein: Kodein = Kodein.lazy {
        import(androidXModule(this@MainApplication))

        bind() from singleton { APIService() }

        bind<PaymentProvider>() with provider {
            PaymentProviderImpl(instance(), getCurrentActivity()!! as MainActivity)
        }

        bind<FirebaseAuthPhone>() with provider {
            FirebaseAuthPhoneImpl()
        }

        bind<RemoteRepository>() with singleton {
            RemoteRepositoryImpl(instance())
        }




        bind<RemoteDataSource>() with singleton {
            RemoteDataSourceImp(instance())
        }


        //Factory
        bind() from singleton { LoginViewModelFactory(instance()) }
        bind() from singleton { AccountViewModelFactory(instance()) }


    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }


}