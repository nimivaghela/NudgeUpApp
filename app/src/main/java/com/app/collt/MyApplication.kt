package com.app.collt

import androidx.multidex.MultiDexApplication
import com.app.collt.domain.models.UserHolder
import com.app.collt.injection.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : MultiDexApplication() {

    lateinit var mUserHolder: UserHolder

    companion object {
        lateinit var instance: MyApplication

        fun getAppContext(): MyApplication {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(appModules)
        }
    }

}