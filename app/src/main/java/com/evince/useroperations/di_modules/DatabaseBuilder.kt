package com.evince.useroperations.di_modules

import android.content.Context
import androidx.room.Room
import com.evince.useroperations.api_services.ApiServices
import com.evince.useroperations.database.AppDatabase
import com.evince.useroperations.database.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/*
* RetrofitBuilder to provide the instances
* */

@Module
@InstallIn(SingletonComponent::class)
class DatabaseBuilder {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "APP_DATABASE"
        ).build()
    }

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.usersDao()
    }
}