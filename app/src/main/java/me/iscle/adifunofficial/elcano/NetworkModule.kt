package me.iscle.adifunofficial.elcano

import android.content.Context
import android.provider.Settings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.iscle.adifunofficial.elcano.circulation.network.CirculationService
import me.iscle.adifunofficial.elcano.stations.network.StationService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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

    @Provides
    @Singleton
    fun provideStationsService(
        @Named("Authenticated") okHttpClient: OkHttpClient
    ): StationService {
        return Retrofit.Builder()
            .baseUrl("https://estaciones.api.adif.es")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(StationService::class.java)
    }

    @Provides
    @Singleton
    fun provideCirculationService(
        @Named("Authenticated") okHttpClient: OkHttpClient
    ): CirculationService {
        return Retrofit.Builder()
            .baseUrl("https://circulacion.api.adif.es")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(CirculationService::class.java)
    }
}