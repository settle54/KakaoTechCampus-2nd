package potatocake.katecam.everymoment.data.model.network.dto.response


import com.google.gson.annotations.SerializedName

data class GetCommentCntResponse(
    @SerializedName("code")
    val code: Int,
    @SerializedName("info")
    val commentCnt: Int,
    @SerializedName("message")
    val message: String
)