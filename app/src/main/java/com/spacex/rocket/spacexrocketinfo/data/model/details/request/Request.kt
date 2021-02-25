package com.spacex.rocket.spacexrocketinfo.data.model.details.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RequestQuery(
    @SerializedName("rocket")
    val rocket: String
) : Parcelable
