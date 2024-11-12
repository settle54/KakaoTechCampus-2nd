package potatocake.katecam.everymoment.data.repository

import android.app.Activity
import android.util.Log
import potatocake.katecam.everymoment.data.model.network.api.NetworkModule
import potatocake.katecam.everymoment.services.location.GlobalApplication
import potatocake.katecam.everymoment.data.model.network.api.NetworkUtil
import potatocake.katecam.everymoment.data.model.network.api.PotatoCakeApiService
import potatocake.katecam.everymoment.data.model.network.dto.response.NonLoginUserNumberResponse
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

    private fun saveAnonymousNumber(number: Int?) {
        GlobalApplication.prefs.setInt(KEY_ANONYMOUS_NUMBER, number)
    }

    private fun getStoredAnonymousNumber(): Int? {
        return GlobalApplication.prefs.getInt(KEY_ANONYMOUS_NUMBER)
    }

    fun getAnonymousLogin(
        callback: (Boolean, NonLoginUserNumberResponse?) -> Unit
    ) {
        val storedNumber = getStoredAnonymousNumber()
        val storedToken = GlobalApplication.prefs.getString("token", null)

        apiService.getAnonymousLogin(storedNumber)
            .enqueue(object : Callback<NonLoginUserNumberResponse> {
                override fun onResponse(
                    call: Call<NonLoginUserNumberResponse>,
                    response: Response<NonLoginUserNumberResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { responseBody ->
                            responseBody.info.number?.let { number ->
                                saveAnonymousNumber(number)
                            }

                            responseBody.info.token?.let { token ->
                                GlobalApplication.prefs.setString("token", token)
                                Log.d("AnonymousLogin", "Token saved: $token")
                            }

                            callback(true, responseBody)
                        } ?: callback(false, null)
                    } else {
                        Log.d("AnonymousLogin", "Response not successful: ${response.code()}")
                        callback(false, null)
                    }
                }

                override fun onFailure(call: Call<NonLoginUserNumberResponse>, t: Throwable) {
                    Log.d("AnonymousLogin", "Failed to AnonymousLogin: ${t.message}")
                    callback(false, null)
                }
            })
    }

    companion object {
        private const val KEY_ANONYMOUS_NUMBER = "anonymous_user_number"
    }
}