package com.aradevs.storagemanager.datasources.implementations

import com.aradevs.domain.Cart
import com.aradevs.domain.Product
import com.aradevs.domain.Purchase
import com.aradevs.domain.coroutines.Status
import com.aradevs.domain.exceptions.ItemAlreadyInCartException
import com.aradevs.storagemanager.AppDatabase
import com.aradevs.storagemanager.data_handling.toDomain
import com.aradevs.storagemanager.data_handling.toEntity
import com.aradevs.storagemanager.datasources.DatabaseLocalDataSource

class DatabaseLocalDataSourceImpl(private val db: AppDatabase) : DatabaseLocalDataSource {
    override suspend fun saveProduct(product: Product): Status<Unit> {
        return try {
            db.getDatabaseDao().saveProduct(product.toEntity())
            Status.Success(Unit)
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    override suspend fun getProducts(): Status<List<Product>> {
        return try {
            val results = db.getDatabaseDao().getProducts()
            Status.Success(results.map { it.toDomain() })
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    override suspend fun getCartProducts(): Status<List<Product>> {
        return try {
            val results = db.getDatabaseDao().getCartProducts()
            Status.Success(results.map { it.toDomain() })
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    override suspend fun saveProductToCart(cart: Cart): Status<Unit> {
        return try {
            val itemCount = db.getDatabaseDao().checkIfIsAlreadyAdded(cart.productId)
            if (itemCount == 0) {
                db.getDatabaseDao().saveProductToCart(cart.toEntity())
                Status.Success(Unit)
            } else {
                Status.Error(ItemAlreadyInCartException())
            }


        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    override suspend fun deleteProductFromCart(productId: Int): Status<Unit> {
        return try {
            db.getDatabaseDao().deleteProductFromCart(productId)
            Status.Success(Unit)
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    override suspend fun setCartToEmpty(): Status<Unit> {
        return try {
            db.getDatabaseDao().setCartToEmpty()
            Status.Success(Unit)
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    override suspend fun savePurchase(purchase: Purchase): Status<Unit> {
        return try {
            db.getDatabaseDao().savePurchase(purchase.toEntity())
            Status.Success(Unit)
        } catch (e: Exception) {
            Status.Error(e)
        }
    }

    override suspend fun getPurchases(): Status<List<Product>> {
        return try {
            val results = db.getDatabaseDao().getPurchases()
            Status.Success(results.map { it.toDomain() })
        } catch (e: Exception) {
            Status.Error(e)
        }
    }
}