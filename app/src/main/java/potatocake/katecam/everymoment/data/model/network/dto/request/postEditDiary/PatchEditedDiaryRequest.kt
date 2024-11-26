package potatocake.katecam.everymoment.data.model.network.dto.request.postEditDiary


import com.google.gson.annotations.SerializedName

data class PatchEditedDiaryRequest(
    @SerializedName("address")
    val address: String,
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("content")
    val content: String,
    @SerializedName("contentDelete")
    val contentDelete: Boolean,
    @SerializedName("deleteAllCategories")
    val deleteAllCategories: Boolean,
    @SerializedName("emoji")
    val emoji: String?,
    @SerializedName("emojiDelete")
    val emojiDelete: Boolean,
    @SerializedName("locationName")
    val locationName: String
)