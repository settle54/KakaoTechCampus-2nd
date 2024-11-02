package com.example.everymoment.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.model.network.dto.response.MyInformation
import com.example.everymoment.data.repository.MyInfoRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val myInfoRepository: MyInfoRepository): ViewModel() {
    private val _myInfo = MutableLiveData<MyInformation>()
    val myInfo: MutableLiveData<MyInformation> get() = _myInfo
    fun fetchMyInfo(){
        viewModelScope.launch {
            myInfoRepository.getMyInfo { success, response ->
                if (success && response != null) {
                    _myInfo.postValue(response.info)
                }
            }
        }
    }
}