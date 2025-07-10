package com.mleon.core.data.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.mleon.core.data.datasource.local.AppDatabase
import com.mleon.core.data.datasource.local.dao.ProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val DB_NAME = "yapp_db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // Preparado para futuras migraciones
        }
    }

    @Provides
    @Singleton //unica BD
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, DB_NAME)
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    //No tienen Singleton porque no los necesitamos durante toda la vida de la app
    @Provides
    fun provideProductDao(database: AppDatabase) : ProductDao = database.productDao()

    @Provides
    fun provideCartItemDao(database: AppDatabase) = database.cartItemDao()

    @Provides
    fun provideOrderDao(database: AppDatabase) = database.orderDao()

    @Provides
    fun provideOrderItemDao(database: AppDatabase) = database.orderItemDao()
}