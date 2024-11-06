package com.example.everymoment.data.model.network.dto.request


import com.google.gson.annotations.SerializedName

data class PostCommentRequest(
    @SerializedName("content")
    val content: String
)