package com.aradevs.storagemanager.modules

import com.aradevs.storagemanager.repositories.DatabaseRepository
import com.aradevs.storagemanager.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class UseCasesModule {
    @Provides
    fun providesSaveProductUseCase(repository: DatabaseRepository): SaveProductUseCase =
        SaveProductUseCase(repository)

    @Provides
    fun providesGetProductsUseCase(repository: DatabaseRepository): GetProductsUseCase =
        GetProductsUseCase(repository)

    @Provides
    fun providesGetCartProductsUseCase(repository: DatabaseRepository): GetCartProductsUseCase =
        GetCartProductsUseCase(repository)

    @Provides
    fun providesSaveProductToCartUseCase(repository: DatabaseRepository): SaveProductToCartUseCase =
        SaveProductToCartUseCase(repository)

    @Provides
    fun providesDeleteProductFromCartUseCase(repository: DatabaseRepository): DeleteProductFromCartUseCase =
        DeleteProductFromCartUseCase(repository)

    @Provides
    fun providesSetCartToEmptyUseCase(repository: DatabaseRepository): SetCartToEmptyUseCase =
        SetCartToEmptyUseCase(repository)

    @Provides
    fun providesSavePurchaseUseCase(repository: DatabaseRepository): SavePurchaseUseCase =
        SavePurchaseUseCase(repository)

    @Provides
    fun providesGetPurchasesUseCase(repository: DatabaseRepository): GetPurchasesUseCase =
        GetPurchasesUseCase(repository)
}