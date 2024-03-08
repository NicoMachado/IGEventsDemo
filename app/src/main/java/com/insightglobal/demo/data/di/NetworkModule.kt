package com.insightglobal.demo.data.di

import com.insightglobal.demo.data.network.DiscoveryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://app.ticketmaster.com/discovery/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideDiscoveryApi(retrofit: Retrofit): DiscoveryApi{
        return retrofit.create(DiscoveryApi::class.java)
    }
}