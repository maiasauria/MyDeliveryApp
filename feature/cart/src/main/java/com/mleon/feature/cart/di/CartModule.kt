package com.mleon.feature.cart.di

import android.content.Context
import com.mleon.feature.cart.view.viewmodel.CartViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class CartModule {
    @Provides
    @Singleton
    fun provideCartViewModel(
        @ApplicationContext context: Context
    ): CartViewModel {
        return CartViewModel()
    }
}