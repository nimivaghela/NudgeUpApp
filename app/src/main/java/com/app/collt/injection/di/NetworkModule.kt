package com.app.collt.injection.di

import android.content.Context
import com.app.collt.BuildConfig
import com.app.collt.MyApplication
import com.app.collt.domain.models.UserHolder
import com.app.collt.helper.AUTHORIZATION
import com.app.collt.helper.authToken
import com.app.collt.shared_data.endpoint.ApiEndPoint
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single { provideHttpLogging(androidContext()) }
    single { provideRetrofit(get()) }
    single { provideApiService(get()) }
}

fun provideHttpLogging(androidContext: Context): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
    return OkHttpClient.Builder().addInterceptor { chain ->
        val request: Request = chain.request().newBuilder()
            .addHeader(
                "authorization",
                provideUserHolder(provideSharedPreference(androidContext)).authToken.toString()
            ).build()
        chain.proceed(request)
    }.connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addNetworkInterceptor(logging)
        .build()
}

fun provideRetrofit(client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(client)
        .build()
}

fun provideApiService(retrofit: Retrofit): ApiEndPoint = retrofit.create(ApiEndPoint::class.java)

