package com.aradevs.taller_practico_2_ma171622_mg171623.views.medicine_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aradevs.domain.Cart
import com.aradevs.domain.coroutines.Status
import com.aradevs.storagemanager.use_cases.SaveProductToCartUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineDetailViewModel @Inject constructor(private val saveProductToCartUseCase: SaveProductToCartUseCase) :
    ViewModel() {
    private val _savingStatus: MutableLiveData<Status<Unit>> = MutableLiveData(Status.Initial())
    val savingStatus: LiveData<Status<Unit>> get() = _savingStatus

    fun saveItemToCart(productId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _savingStatus.postValue(Status.Loading())
            when (val status = saveProductToCartUseCase(Cart(0, productId))) {
                is Status.Success -> _savingStatus.postValue(Status.Success(Unit))
                is Status.Error -> _savingStatus.postValue(Status.Error(status.exception))
                else -> {
                    //do nothing
                }
            }
        }
    }
}