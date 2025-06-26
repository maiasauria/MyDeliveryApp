package com.mleon.core.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    //TODO revisar si se usa.
    @Provides
    @Singleton
    fun provideApplicationContext(
        @ApplicationContext context: Context,
    ): Context = context

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext appContext: Context,
    ): SharedPreferences = appContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

}
