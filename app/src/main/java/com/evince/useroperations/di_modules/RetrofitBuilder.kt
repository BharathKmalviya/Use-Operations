package com.evince.useroperations.di_modules

import android.content.Context
import com.evince.useroperations.api_services.ApiServices
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
class RetrofitBuilder {

    // this provides the okHttpClient instance for the retrofit
    @Provides
    @Singleton
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient {
        //logging interceptor
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.apply {
            connectTimeout(60L, TimeUnit.SECONDS)
            readTimeout(60L, TimeUnit.SECONDS)
            writeTimeout(60L, TimeUnit.SECONDS)
            addInterceptor(loggingInterceptor)
        }
        return okHttpClient.build()
    }

    // this provides the retrofit instance to use in the api services
    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://reqres.in/api/")
            .client(okHttpClient)
            .build()
    }

    // this provides the api services instance to use in the repository
    @Provides
    @Singleton
    fun providesApService(retrofit: Retrofit): ApiServices {
        return retrofit.create(ApiServices::class.java)
    }
}