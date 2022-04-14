package com.aradevs.storagemanager.datasources

import com.aradevs.domain.Cart
import com.aradevs.domain.Product
import com.aradevs.domain.Purchase
import com.aradevs.domain.coroutines.Status

interface DatabaseLocalDataSource {
    suspend fun saveProduct(product: Product): Status<Unit>
    suspend fun getProducts(): Status<List<Product>>
    suspend fun getCartProducts(): Status<List<Product>>
    suspend fun saveProductToCart(cart: Cart): Status<Unit>
    suspend fun deleteProductFromCart(productId: Int): Status<Unit>
    suspend fun setCartToEmpty(): Status<Unit>
    suspend fun savePurchase(purchase: Purchase): Status<Unit>
    suspend fun getPurchases(): Status<List<Product>>
}