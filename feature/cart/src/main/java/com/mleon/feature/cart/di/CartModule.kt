package com.mleon.feature.cart.di

import com.mleon.core.data.repository.interfaces.CartItemRepository
import com.mleon.feature.cart.view.viewmodel.CartViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class CartModule {
    @Provides
    @Singleton
    fun provideCartViewModel(
        cartItemRepository: CartItemRepository
    ): CartViewModel {
        return CartViewModel(cartItemRepository)
    }
}