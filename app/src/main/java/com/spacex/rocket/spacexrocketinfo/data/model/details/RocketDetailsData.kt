package com.spacex.rocket.spacexrocketinfo.data.model.details


import com.google.gson.annotations.SerializedName

data class RocketDetailsData(
    @SerializedName("docs")
    val docs: List<Doc>,
    @SerializedName("hasNextPage")
    val hasNextPage: Boolean,
    @SerializedName("hasPrevPage")
    val hasPrevPage: Boolean,
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("nextPage")
    val nextPage: Int,
    @SerializedName("offset")
    val offset: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("pagingCounter")
    val pagingCounter: Int,
    @SerializedName("prevPage")
    val prevPage: Any,
    @SerializedName("totalDocs")
    val totalDocs: Int,
    @SerializedName("totalPages")
    val totalPages: Int
)