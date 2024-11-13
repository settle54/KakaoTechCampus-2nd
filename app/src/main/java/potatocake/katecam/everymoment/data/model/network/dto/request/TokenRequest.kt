package potatocake.katecam.everymoment.data.model.network.dto.request

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("fcmToken")
    val fcmToken: String,
    @SerializedName("deviceId")
    val deviceId: String
)