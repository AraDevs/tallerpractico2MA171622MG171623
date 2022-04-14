package com.aradevs.taller_practico_2_ma171622_mg171623.views.purchase_history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aradevs.domain.Product
import com.aradevs.domain.coroutines.Status
import com.aradevs.storagemanager.use_cases.GetPurchasesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PurchaseHistoryViewModel @Inject constructor(private val getPurchasesUseCase: GetPurchasesUseCase) :
    ViewModel() {
    private var _job: Job? = null
    private val _productStatus: MutableLiveData<Status<List<Product>>> =
        MutableLiveData(Status.Loading())
    val productStatus: LiveData<Status<List<Product>>> get() = _productStatus

    fun getPurchases() {
        _job?.cancel()
        _job = viewModelScope.launch(Dispatchers.IO) {
            when (val status = getPurchasesUseCase()) {
                is Status.Success -> {
                    _productStatus.postValue(status)
                }
                else -> _productStatus.postValue(status)
            }
        }
    }
}