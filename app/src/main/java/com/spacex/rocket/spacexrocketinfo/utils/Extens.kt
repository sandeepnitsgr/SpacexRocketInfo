package com.spacex.rocket.spacexrocketinfo.utils

import com.spacex.rocket.spacexrocketinfo.data.model.RocketInfo
import com.spacex.rocket.spacexrocketinfo.data.model.RocketListData
import com.spacex.rocket.spacexrocketinfo.domain.mapper.MappedRocketInfo
import com.spacex.rocket.spacexrocketinfo.domain.mapper.MappedRocketListData


fun RocketListData.toMappedRocketListData(): MappedRocketListData {
    val mappedRocketListData = MappedRocketListData()
    all { info -> mappedRocketListData.add(info.toMappedRocketInfo()) }
    return mappedRocketListData
}

fun RocketInfo.toMappedRocketInfo(): MappedRocketInfo {
    return MappedRocketInfo(name, country, engines?.number, flickrImages?.get(0))
}