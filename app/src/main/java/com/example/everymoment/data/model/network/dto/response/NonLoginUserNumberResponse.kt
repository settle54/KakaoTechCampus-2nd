package com.example.everymoment.data.model.network.dto.response

import com.google.gson.annotations.SerializedName

data class NonLoginUserNumberResponse(
    val code: Int,
    val message: String,
    val info: Info
) {
    data class Info(
        val number: Int,
        val token: String
    )
}