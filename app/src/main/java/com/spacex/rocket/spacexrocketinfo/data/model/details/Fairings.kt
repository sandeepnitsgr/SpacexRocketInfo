package com.spacex.rocket.spacexrocketinfo.data.model.details


import com.google.gson.annotations.SerializedName

data class Fairings(
    @SerializedName("recovered")
    val recovered: Boolean,
    @SerializedName("recovery_attempt")
    val recoveryAttempt: Boolean,
    @SerializedName("reused")
    val reused: Boolean,
    @SerializedName("ships")
    val ships: List<Any>
)