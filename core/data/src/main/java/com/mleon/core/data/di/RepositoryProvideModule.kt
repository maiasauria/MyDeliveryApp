package com.mleon.core.data.di

import android.content.Context
import android.content.SharedPreferences
import com.mleon.core.data.remote.OrderApiService
import com.mleon.core.data.remote.ProductsApiService
import com.mleon.core.data.remote.RetrofitClient
import com.mleon.core.data.remote.UsersApiService
import com.mleon.core.data.repository.OrdersRepositoryApi
import com.mleon.core.data.repository.ProductRepositoryApi
import com.mleon.core.data.repository.UserRepositoryApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryProvideModule {

    //TODO dividir en modulos

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

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences =
        appContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideUserRepository(apiService: UsersApiService): UserRepositoryApi =
        UserRepositoryApi(apiService)

    @Provides
    @Singleton
    fun provideOrdersRepository(apiService: OrderApiService): OrdersRepositoryApi =
        OrdersRepositoryApi(apiService)

    @Provides
    @Singleton
    fun provideProductRepositoryApi(apiService: ProductsApiService): ProductRepositoryApi =
        ProductRepositoryApi(apiService)

}
