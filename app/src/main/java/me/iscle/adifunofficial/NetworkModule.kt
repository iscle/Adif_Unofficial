package me.iscle.adifunofficial

import android.content.Context
import android.provider.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideOkHttpClientBuilder(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(builder: OkHttpClient.Builder): OkHttpClient {
        return builder.build()
    }

    @Provides
    @Singleton
    @Named("Authenticated")
    fun provideAuthenticatedOkHttpClient(
        @ApplicationContext context: Context,
        builder: OkHttpClient.Builder
    ): OkHttpClient {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        return builder
            .addInterceptor(ElcanoAuthInterceptor(androidId))
            .build()
    }
}