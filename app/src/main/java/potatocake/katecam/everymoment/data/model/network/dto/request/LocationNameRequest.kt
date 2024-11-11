package potatocake.katecam.everymoment.data.model.network.dto.request

import com.google.gson.annotations.SerializedName

data class LocationNameRequest(
    @SerializedName("locationName")
    val locationName: String?
)
