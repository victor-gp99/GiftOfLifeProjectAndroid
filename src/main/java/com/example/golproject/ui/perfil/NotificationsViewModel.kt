package com.example.golproject.ui.perfil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationsViewModel : ViewModel() {
    val data = MutableLiveData<String>()
    fun setData(newData: String){
        this.data.value=newData
    }

}