package potatocake.katecam.everymoment.data.model.network.dto.request

import potatocake.katecam.everymoment.data.model.network.dto.request.postEditDiary.Category
import potatocake.katecam.everymoment.data.model.network.dto.vo.LocationPoint

data class ManualDiaryRequest(
    val diaryDate: String?,
    val categories: MutableList<Category>,
    val locationPoint: LocationPoint,
    val locationName: String,
    val address: String,
    val emoji: String?,
    val content: String?,
    val public: Boolean,
    val bookmark: Boolean
)