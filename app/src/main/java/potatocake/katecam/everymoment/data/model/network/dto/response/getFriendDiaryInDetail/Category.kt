package potatocake.katecam.everymoment.data.model.network.dto.response.getFriendDiaryInDetail


import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("categoryName")
    val categoryName: String,
    @SerializedName("id")
    val id: Int
)