package com.spacex.rocket.spacexrocketinfo.data.remote.request

import com.google.gson.annotations.SerializedName

data class RocketQuery(
    @SerializedName("rocket")
    val rocket: String
    )
