package com.aradevs.storagemanager.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.aradevs.storagemanager.CartEntity
import com.aradevs.storagemanager.ProductEntity
import com.aradevs.storagemanager.PurchasesEntity

@Dao
interface DatabaseDao {
    @Insert
    suspend fun saveProduct(productEntity: ProductEntity)

    @Query("select * from products")
    suspend fun getProducts(): List<ProductEntity>

    //region shopping cart
    @Query("select products.* from products, shopping_cart where products.id = shopping_cart.product_id")
    suspend fun getCartProducts(): List<ProductEntity>

    @Query("select count(1) from shopping_cart where shopping_cart.product_id = :productId")
    suspend fun checkIfIsAlreadyAdded(productId: Int): Int

    @Insert
    suspend fun saveProductToCart(cartEntity: CartEntity)

    @Query("delete from shopping_cart where shopping_cart.product_id = :productId")
    suspend fun deleteProductFromCart(productId: Int)

    @Query("delete from shopping_cart")
    suspend fun setCartToEmpty()
    //endregion

    //region purchase history
    @Insert
    suspend fun savePurchase(purchasesEntity: PurchasesEntity)

    @Query("select products.* from products, purchases where products.id = purchases.product_id")
    suspend fun getPurchases(): List<ProductEntity>
    //endregion purchase history
}