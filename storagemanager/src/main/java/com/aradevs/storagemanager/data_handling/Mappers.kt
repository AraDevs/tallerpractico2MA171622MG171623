package com.aradevs.storagemanager.data_handling

import com.aradevs.domain.Cart
import com.aradevs.domain.Product
import com.aradevs.domain.Purchase
import com.aradevs.storagemanager.CartEntity
import com.aradevs.storagemanager.ProductEntity
import com.aradevs.storagemanager.PurchasesEntity

fun ProductEntity.toDomain(): Product =
    Product(id = this.id,
        name = this.name,
        description = this.description,
        price = this.price,
        image = this.image)

fun Product.toEntity(): ProductEntity =
    ProductEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        price = this.price,
        image = this.image
    )

fun CartEntity.toDomain(): Cart = Cart(id = this.id, productId = this.productId)

fun Cart.toEntity(): CartEntity = CartEntity(id = this.id, productId = this.productId)

fun PurchasesEntity.toDomain(): Purchase = Purchase(id = this.id, productId = this.productId)

fun Purchase.toEntity(): PurchasesEntity = PurchasesEntity(id = this.id, productId = this.productId)