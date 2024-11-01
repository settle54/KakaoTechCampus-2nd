package com.example.everymoment.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.everymoment.data.model.network.api.GooglePlaceApiUtil
import com.example.everymoment.data.repository.DiaryRepository
import com.example.everymoment.data.model.network.dto.response.Diary
import kotlinx.coroutines.launch

class TimelineViewModel(private val diaryRepository: DiaryRepository) : ViewModel() {
    private val _diaries = MutableLiveData<List<Diary>>()
    val diaries: LiveData<List<Diary>> get() = _diaries


    fun fetchDiaries(date: String) {
        viewModelScope.launch {
            diaryRepository.getDiaries(date) { success, response ->
                if (success && response != null) {
                    _diaries.postValue(response.info.diaries)
                }
            }
        }
    }

    fun updateBookmarkStatus(diaryId: Int) {
        viewModelScope.launch {
            diaryRepository.updateBookmarkStatus(diaryId) { success, response ->
            }
        }
    }

    fun updateShareStatus(diaryId: Int) {
        viewModelScope.launch {
            diaryRepository.updateShareStatus(diaryId) { success, response ->
            }
        }
    }

    fun deleteDiary(diaryId: Int){
        viewModelScope.launch {
            diaryRepository.deleteDiary(diaryId) { success, response -> }
        }
    }

    fun getPlaceNamesForDiary(diaryId: Int, callback: (List<String>) -> Unit) {
        getDiaryLocation(diaryId) { latitude, longitude ->
            if (latitude == 0.0 && longitude == 0.0) {
                callback(listOf("우리집", "회사", "학교"))
            } else {
                getPlaceNames(latitude, longitude) { placeNames ->
                    callback(placeNames)
                }
            }
        }
    }

    private fun getDiaryLocation(diaryId: Int, callback: (Double, Double) -> Unit) {
        viewModelScope.launch {
            diaryRepository.getDiaryLocation(diaryId) { success, response ->
                if (success && response != null) {
                    callback(response.info.latitude, response.info.longitude)
                } else {
                    callback(0.0, 0.0)
                }
            }
        }
    }

    private fun getPlaceNames(latitude: Double, longitude: Double, callback: (List<String>) -> Unit) {
        GooglePlaceApiUtil.getPlaceNamesFromCoordinates(longitude, latitude) { placeNames, _ ->
            callback(placeNames)
            Log.d("arieum", "google place names : $placeNames")
        }
    }

}