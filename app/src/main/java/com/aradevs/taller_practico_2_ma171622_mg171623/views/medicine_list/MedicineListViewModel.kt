package com.aradevs.taller_practico_2_ma171622_mg171623.views.medicine_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aradevs.domain.Product
import com.aradevs.domain.coroutines.Status
import com.aradevs.storagemanager.use_cases.GetProductsUseCase
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineListViewModel @Inject constructor(private val getProductsUseCase: GetProductsUseCase) :
    ViewModel() {
    private var _job: Job? = null
    private val _productStatus: MutableLiveData<Status<List<Product>>> =
        MutableLiveData(Status.Loading())
    val productStatus: LiveData<Status<List<Product>>> get() = _productStatus

    fun getMedicines() {
        _job?.cancel()
        _job = viewModelScope.launch(Dispatchers.IO) {
            when (val status = getProductsUseCase()) {
                is Status.Success -> {
                    if (status.data.isNullOrEmpty() || status.data.size < 10) {
                        getMedicines()
                    } else {
                        _productStatus.postValue(status)
                    }
                }
                else -> _productStatus.postValue(status)
            }
        }
    }
}