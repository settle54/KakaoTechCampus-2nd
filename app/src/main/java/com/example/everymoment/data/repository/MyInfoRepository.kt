package com.example.everymoment.data.repository

import android.util.Log
import com.example.everymoment.data.model.network.api.NetworkModule
import com.example.everymoment.data.model.network.api.PotatoCakeApiService
import com.example.everymoment.data.model.network.dto.response.MyInformationResponse
import com.example.everymoment.services.location.GlobalApplication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyInfoRepository {
    private val apiService: PotatoCakeApiService =
        NetworkModule.provideApiService(NetworkModule.provideRetrofit())
    private val jwtToken = GlobalApplication.prefs.getString("token", "null")
    private val token =
        "Bearer $jwtToken"

    fun getMyInfo(
        callback: (Boolean, MyInformationResponse?) -> Unit
    ) {
        apiService.getMyInfo(token).enqueue(object : Callback<MyInformationResponse> {
            override fun onResponse(
                p0: Call<MyInformationResponse>,
                p1: Response<MyInformationResponse>
            ) {
                if (p1.isSuccessful) {
                    callback(true, p1.body())
                    Log.d("arieum", p1.body().toString())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<MyInformationResponse>, p1: Throwable) {
                Log.d("arieum", "Failed to fetch myinfo: ${p1.message}")
                callback(false, null)
            }
        })
    }
}