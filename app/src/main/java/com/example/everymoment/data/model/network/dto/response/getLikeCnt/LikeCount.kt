package com.example.everymoment.data.model.network.dto.response.getLikeCnt


import com.google.gson.annotations.SerializedName

data class LikeCount(
    @SerializedName("likeCount")
    val likeCount: Int
)