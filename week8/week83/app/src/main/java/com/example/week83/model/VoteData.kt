package com.example.week83.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class VoteData(
    @SerialName("image_id") val imageId: String? = null,
    @SerialName("sub_id") val subId: String? = null,
    val value: Int? = null
)