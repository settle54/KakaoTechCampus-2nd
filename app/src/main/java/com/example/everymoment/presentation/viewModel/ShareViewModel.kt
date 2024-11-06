package com.example.everymoment.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.model.network.dto.response.Diary
import com.example.everymoment.data.repository.FriendDiaryRepository
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.data.model.network.dto.response.Friends
import kotlinx.coroutines.launch

class ShareViewModel(
    private val friendDiaryRepository: FriendDiaryRepository,
    private val friendRepository: FriendRepository
) : ViewModel() {
    private val _selectedFriendName = MutableLiveData<String>()
    val selectedFriendName: LiveData<String> get() = _selectedFriendName
    private val _friends = MutableLiveData<List<Friends>>()
    val friends: LiveData<List<Friends>> get() = _friends

    private val _diaries = MutableLiveData<List<Diary>>()
    val diaries: LiveData<List<Diary>> get() = _diaries

    fun setSelectedFriendName(nickName: String) {
        viewModelScope.launch {
            _selectedFriendName.postValue(nickName)
        }
    }

    fun fetchFriendsList() {
        viewModelScope.launch {
            friendRepository.getFriendsList() { success, response ->
                if (success && response != null) {
                    _friends.postValue(response.info.friends)
                }
            }
        }
    }

    fun fetchFriendDiaryList(friendId: Int) {
        viewModelScope.launch {
            friendDiaryRepository.getFriendDiaries(friendId) { success, response ->
                if (success && response != null) {
                    _diaries.postValue(response.info.diaries)
                }
            }
        }
    }

    fun fetchTotalFriendDiaryList(date: String){
        viewModelScope.launch {
            friendDiaryRepository.getTotalFriendDiaries(date) { success, response ->
                if (success && response != null) {
                    _diaries.postValue(response.info.diaries)
                }
            }
        }
    }
}