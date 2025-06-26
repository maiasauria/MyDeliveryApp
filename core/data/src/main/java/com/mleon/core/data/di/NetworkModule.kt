package com.mleon.core.data.di

import com.mleon.core.data.remote.OrderApiService
import com.mleon.core.data.remote.ProductsApiService
import com.mleon.core.data.remote.RetrofitClient
import com.mleon.core.data.remote.UsersApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(RetrofitClient.url)
            .client(RetrofitClient.okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideProductsApiService(retrofit: Retrofit): ProductsApiService =
        retrofit.create(ProductsApiService::class.java)

    @Provides
    @Singleton
    fun provideUsersApiService(retrofit: Retrofit): UsersApiService =
        retrofit.create(UsersApiService::class.java)

    @Provides
    @Singleton
    fun provideOrderApiService(retrofit: Retrofit): OrderApiService =
        retrofit.create(OrderApiService::class.java)
}