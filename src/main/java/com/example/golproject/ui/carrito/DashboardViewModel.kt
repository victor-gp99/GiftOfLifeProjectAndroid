package com.example.golproject.ui.carrito

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {
    val data = MutableLiveData<Int>()
    fun setData(newData: Int){
        this.data.value =newData
    }
}