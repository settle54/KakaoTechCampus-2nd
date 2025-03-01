package potatocake.katecam.everymoment.data.model.network.dto.response

import com.google.gson.annotations.SerializedName

data class FriendRequestListResponse(
    val code: Int,
    val message: String,
    val info: FriendRequestListInfo
)

data class FriendRequestListInfo(
    val friendRequests: List<FriendRequests>,
    val next: Int
)

data class FriendRequests(
    @SerializedName("id")
    val id: Int,

    @SerializedName("senderId")
    val senderId: Int,

    @SerializedName("nickname")
    val nickname: String,

    @SerializedName("profileImageUrl")
    val profileImageUrl: String?
)
