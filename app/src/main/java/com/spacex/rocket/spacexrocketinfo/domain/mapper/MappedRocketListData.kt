package com.spacex.rocket.spacexrocketinfo.domain.mapper

data class MappedRocketInfo(
    val name: String?,
    val country: String?,
    val engines: Int?,
    val flickrImg: String?
)

class MappedRocketListData : ArrayList<MappedRocketInfo>()
