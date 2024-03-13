package com.app.collt.injection.di

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.app.collt.domain.models.UserHolder
import org.koin.dsl.module

val sharedPreferenceModule = module {
    single { provideSharedPreference(get()) }
    single { provideUserHolder(get()) }
}

fun provideUserHolder(sharedPreferences: SharedPreferences): UserHolder =
    UserHolder(sharedPreferences)

fun provideSharedPreference(context: Context): SharedPreferences {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    val sharedPrefsFile = "secret_shared_prefs"
    return EncryptedSharedPreferences.create(
        sharedPrefsFile,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}