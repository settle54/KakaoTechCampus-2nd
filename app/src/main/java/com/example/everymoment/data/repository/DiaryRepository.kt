package com.example.everymoment.data.repository

import android.util.Log
import com.example.everymoment.data.model.NetworkUtil

class DiaryRepository {
    private val jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6MywiaWF0IjoxNzI4Nzk3NDg1LCJleHAiOjE3Mjg5NzAyODV9.28nxIHOKBHQ2WsUAdbsNokuB-96gNFyKkJPOLKfxuic"

    fun getDiaries(
        date: String,
        callback: (Boolean, DiaryResponse?) -> Unit
    ) {
        val url = "http://13.125.156.74:8080/api/diaries/my"
        val params = mapOf("date" to date)

        NetworkUtil.getData(
            url,
            jwtToken,
            params,
            DiaryResponse::class.java
        ) { success, response ->
            Log.d("arieum","$response")
            callback(success, response)
        }
    }

    fun updateBookmarkStatus(
        diaryId: Int,
        callback: (Boolean, String?) -> Unit
    ) {
        val url = "http://13.125.156.74:8080/api/diaries/$diaryId/bookmark"

        NetworkUtil.patchRequest(
            url,
            jwtToken,
            null
        ) { success, message ->
            Log.d("arieum", "$message")
            callback(success, message)
        }
    }

    fun updateShareStatus(
        diaryId: Int,
        callback: (Boolean, String?) -> Unit
    ) {
        val url = "http://13.125.156.74:8080/api/diaries/$diaryId/privacy"

        NetworkUtil.patchRequest(
            url,
            jwtToken,
            null
        ) { success, message ->
            Log.d("arieum", "$message")
            callback(success, message)
        }
    }

    fun deleteDiary(
        diaryId: Int,
        callback: (Boolean, String?) -> Unit
    ) {
        val url = "http://13.125.156.74:8080/api/diaries/$diaryId"

        NetworkUtil.deleteRequest(
            url,
            jwtToken,
            null
        ) { success, response ->
            Log.d("arieum", "$response")
            callback(success, response)
        }
    }
}