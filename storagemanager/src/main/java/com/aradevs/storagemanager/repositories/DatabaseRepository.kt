package com.aradevs.storagemanager.repositories

import com.aradevs.domain.Cart
import com.aradevs.domain.Product
import com.aradevs.domain.Purchase
import com.aradevs.storagemanager.datasources.DatabaseLocalDataSource

class DatabaseRepository(private val databaseLocalDataSource: DatabaseLocalDataSource) {
    suspend fun saveProduct(product: Product) =
        databaseLocalDataSource.saveProduct(product)

    suspend fun getProducts() = databaseLocalDataSource.getProducts()

    suspend fun getCartProducts() = databaseLocalDataSource.getCartProducts()

    suspend fun saveProductToCart(cart: Cart) = databaseLocalDataSource.saveProductToCart(cart)

    suspend fun deleteProductFromCart(productId: Int) =
        databaseLocalDataSource.deleteProductFromCart(productId)

    suspend fun setCartToEmpty() = databaseLocalDataSource.setCartToEmpty()

    suspend fun savePurchase(purchase: Purchase) = databaseLocalDataSource.savePurchase(purchase)

    suspend fun getPurchases() = databaseLocalDataSource.getPurchases()
}