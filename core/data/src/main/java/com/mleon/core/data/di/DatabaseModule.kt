package com.mleon.core.data.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import com.mleon.core.data.datasource.local.AppDatabase
import com.mleon.core.data.datasource.local.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton //unica BD
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "db")
            .setQueryCallback({ sqlQuery, bindArgs ->
                Log.d("RoomQuery", "SQL: $sqlQuery, Args: $bindArgs")
            }, Executors.newSingleThreadExecutor())
            .build()
    }

    //No tienen Singleton porque no los necesitamos durante toda la vida de la app
    @Provides
    fun provideProductDao(database: AppDatabase) : ProductDao = database.productDao()

    @Provides
    fun provideCartItemDao(database: AppDatabase) = database.cartItemDao()

    //@Provides
    //fun provideOrderHistoryDao(database: AppDatabase) = database.orderHistoryDao()

    @Provides
    fun provideOrderDao(database: AppDatabase) = database.orderDao()

    @Provides
    fun provideOrderItemDao(database: AppDatabase) = database.orderItemDao()
}