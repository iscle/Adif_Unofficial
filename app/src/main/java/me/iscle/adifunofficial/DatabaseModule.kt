package me.iscle.adifunofficial

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.iscle.adifunofficial.station.StationDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "adif_unofficial.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideStationDao(
        appDatabase: AppDatabase,
    ): StationDao {
        return appDatabase.stationDao()
    }
}