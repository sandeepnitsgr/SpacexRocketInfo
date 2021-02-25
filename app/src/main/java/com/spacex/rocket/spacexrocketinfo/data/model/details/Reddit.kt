package com.spacex.rocket.spacexrocketinfo.data.model.details


import com.google.gson.annotations.SerializedName

data class Reddit(
    @SerializedName("campaign")
    val campaign: Any,
    @SerializedName("launch")
    val launch: Any,
    @SerializedName("media")
    val media: Any,
    @SerializedName("recovery")
    val recovery: Any
)