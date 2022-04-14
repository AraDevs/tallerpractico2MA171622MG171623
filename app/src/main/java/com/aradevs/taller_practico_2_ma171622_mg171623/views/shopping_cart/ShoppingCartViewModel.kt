package com.aradevs.taller_practico_2_ma171622_mg171623.views.shopping_cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aradevs.domain.Product
import com.aradevs.domain.Purchase
import com.aradevs.domain.coroutines.Status
import com.aradevs.storagemanager.use_cases.DeleteProductFromCartUseCase
import com.aradevs.storagemanager.use_cases.GetCartProductsUseCase
import com.aradevs.storagemanager.use_cases.SavePurchaseUseCase
import com.aradevs.storagemanager.use_cases.SetCartToEmptyUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel @Inject constructor(
    private val getCartProductsUseCase: GetCartProductsUseCase,
    private val deleteProductFromCartUseCase: DeleteProductFromCartUseCase,
    private val savePurchaseUseCase: SavePurchaseUseCase,
    private val setCartToEmptyUseCase: SetCartToEmptyUseCase,
) :
    ViewModel() {
    private val _productStatus: MutableLiveData<Status<List<Product>>> =
        MutableLiveData(Status.Initial())
    val productStatus: LiveData<Status<List<Product>>> get() = _productStatus

    private val _transactionStatus: MutableLiveData<Status<Unit>> =
        MutableLiveData(Status.Initial())
    val transactionStatus: LiveData<Status<Unit>> get() = _transactionStatus

    fun getShoppingCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            _productStatus.postValue(Status.Loading())
            when (val status = getCartProductsUseCase()) {
                is Status.Success -> _productStatus.postValue(status)
                is Status.Error -> {
                    Timber.d(status.exception.message)
                    _productStatus.postValue(status)
                }
                else -> {
                    //do nothing
                }
            }
        }
    }

    fun deleteProductFromCart(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (deleteProductFromCartUseCase(productId)) {
                is Status.Success -> getShoppingCartItems()
                else -> {
                    //do nothing
                }
            }
        }
    }

    fun placeOrder(items: List<Product>) {
        viewModelScope.launch(Dispatchers.IO) {
            _transactionStatus.postValue(Status.Loading())
            items.forEach {
                savePurchaseUseCase(Purchase(0, it.id))
            }
            _transactionStatus.postValue(Status.Success(Unit))
            setCartToEmptyUseCase().also {
                getShoppingCartItems()
            }
        }
    }
}