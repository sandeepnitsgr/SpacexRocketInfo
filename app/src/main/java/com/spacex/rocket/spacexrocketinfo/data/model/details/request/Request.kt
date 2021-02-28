package com.spacex.rocket.spacexrocketinfo.data.model.details.request

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Request(
    @SerializedName("query")
    val query: RequestQuery,
    @SerializedName("options")
    var option: PagingOption? = null
) : Parcelable

@Parcelize
data class PagingOption(
    @SerializedName("page")
    val page: Int = 1
) : Parcelable

@Parcelize
data class RequestQuery(
    @SerializedName("rocket")
    val rocket: String
) : Parcelable
