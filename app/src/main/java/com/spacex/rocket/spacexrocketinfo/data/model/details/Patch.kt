package com.spacex.rocket.spacexrocketinfo.data.model.details


import com.google.gson.annotations.SerializedName

data class Patch(
    @SerializedName("large")
    val large: String?,
    @SerializedName("small")
    val small: String?
)