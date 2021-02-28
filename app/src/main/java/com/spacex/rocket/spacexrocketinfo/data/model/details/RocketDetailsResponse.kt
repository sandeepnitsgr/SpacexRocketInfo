package com.spacex.rocket.spacexrocketinfo.data.model.details

import com.spacex.rocket.spacexrocketinfo.data.model.LaunchDetailContainer
import com.spacex.rocket.spacexrocketinfo.data.model.Status

class RocketDetailsResponse private constructor(
    val status: Status,
    val data: LaunchDetailContainer?,
    val error: Throwable?,
    var page: Int = 1
) {
    companion object {
        @JvmStatic
        fun success(data: LaunchDetailContainer?, page : Int): RocketDetailsResponse {
            return RocketDetailsResponse(Status.SUCCESS, data, null, page)
        }

        @JvmStatic
        fun error(error: Throwable): RocketDetailsResponse {
            return RocketDetailsResponse(Status.ERROR, null, error)
        }
    }
}