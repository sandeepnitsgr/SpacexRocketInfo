package com.spacex.rocket.spacexrocketinfo.data.model.details


import com.google.gson.annotations.SerializedName

data class Core(
    @SerializedName("core")
    val core: String,
    @SerializedName("flight")
    val flight: Int,
    @SerializedName("gridfins")
    val gridfins: Boolean,
    @SerializedName("landing_attempt")
    val landingAttempt: Boolean,
    @SerializedName("landing_success")
    val landingSuccess: Any,
    @SerializedName("landing_type")
    val landingType: Any,
    @SerializedName("landpad")
    val landpad: Any,
    @SerializedName("legs")
    val legs: Boolean,
    @SerializedName("reused")
    val reused: Boolean
)