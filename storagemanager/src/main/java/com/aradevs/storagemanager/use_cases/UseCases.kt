package com.aradevs.storagemanager.use_cases

import com.aradevs.domain.Cart
import com.aradevs.domain.Product
import com.aradevs.domain.Purchase
import com.aradevs.storagemanager.repositories.DatabaseRepository

class SaveProductUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(product: Product) =
        repository.saveProduct(product)
}

class GetProductsUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke() = repository.getProducts()
}

class GetCartProductsUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke() = repository.getCartProducts()
}

class SaveProductToCartUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(cart: Cart) = repository.saveProductToCart(cart)
}

class DeleteProductFromCartUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(productId: Int) = repository.deleteProductFromCart(productId)
}

class SetCartToEmptyUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke() = repository.setCartToEmpty()
}

class SavePurchaseUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke(purchase: Purchase) = repository.savePurchase(purchase)
}

class GetPurchasesUseCase(private val repository: DatabaseRepository) {
    suspend operator fun invoke() = repository.getPurchases()
}



