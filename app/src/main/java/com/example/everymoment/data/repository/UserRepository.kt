package com.example.everymoment.data.repository

import android.app.Activity
import android.util.Log
import com.example.everymoment.data.model.network.api.NetworkModule
import com.example.everymoment.services.location.GlobalApplication
import com.example.everymoment.data.model.network.api.NetworkUtil
import com.example.everymoment.data.model.network.api.PotatoCakeApiService
import com.example.everymoment.data.model.network.dto.response.NonLoginUserNumberResponse
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.kakao.sdk.user.model.AccessTokenInfo
import com.kakao.sdk.user.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository {
    private val apiService: PotatoCakeApiService =
        NetworkModule.provideApiService(NetworkModule.provideRetrofit())
    private val jwtToken = GlobalApplication.prefs.getString("token", "null")
    private val token =
        "Bearer $jwtToken"

    fun getKakaoTokenInfo(callback: (AccessTokenInfo?, Throwable?) -> Unit) {
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            callback(tokenInfo, error)
        }
    }

    fun loginWithKakaoTalk(
        activity: Activity,
        callback: (OAuthToken?, Throwable?) -> Unit
    ) {
        UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
            callback(token, error)
        }
    }

    fun loginWithKakaoAccount(
        activity: Activity,
        callback: (OAuthToken?, Throwable?) -> Unit
    ) {
        UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)
    }

    fun requestUserInfo(callback: (User?, Throwable?) -> Unit) {
        UserApiClient.instance.me { user, error ->
            callback(user, error)
        }
    }

    fun requestToken(userId: Long?, nickname: String?) {
        NetworkUtil.sendData(
            "http://13.125.156.74:8080/api/members/login",
            null,
            mapOf(
                "number" to userId,
                "nickname" to nickname
            )
        ) { success, code, message, infoObject->
            if (success) {
                val token = infoObject?.get("token")?.asString

                if (token != null) {
                    GlobalApplication.prefs.setString("token", token)
                }

                Log.d("arieum", "서버 응답: $token")
            } else {
                Log.d("arieum", "Network failed")
            }
        }
    }

    fun getAnonymousLogin(
        callback: (Boolean, NonLoginUserNumberResponse?) -> Unit
    ) {
        apiService.getAnonymousLogin().enqueue(object : Callback<NonLoginUserNumberResponse> {
            override fun onResponse(
                p0: Call<NonLoginUserNumberResponse>,
                p1: Response<NonLoginUserNumberResponse>
            ) {
                if (p1.isSuccessful) {
                    callback(true, p1.body())
                    Log.d("AnonymousLogin", p1.body().toString())
                } else {
                    callback(false, null)
                }
            }

            override fun onFailure(p0: Call<NonLoginUserNumberResponse>, p1: Throwable) {
                Log.d("AnonymousLogin", "Failed to AnonymousLogin: ${p1.message}")
                callback(false, null)
            }
        })
    }
}