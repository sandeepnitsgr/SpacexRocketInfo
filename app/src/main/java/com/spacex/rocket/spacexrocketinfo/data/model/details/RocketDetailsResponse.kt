package com.spacex.rocket.spacexrocketinfo.data.model.details

import com.spacex.rocket.spacexrocketinfo.data.model.LaunchDetailContainer
import com.spacex.rocket.spacexrocketinfo.data.model.Status

class RocketDetailsResponse private constructor(
    val status: Status,
    val data: LaunchDetailContainer?,
    val error: Throwable?
) {
    companion object {
        @JvmStatic
        internal fun loading(): RocketDetailsResponse {
            return RocketDetailsResponse(Status.LOADING, null, null)
        }

        @JvmStatic
        fun success(data: LaunchDetailContainer): RocketDetailsResponse {
            return RocketDetailsResponse(Status.SUCCESS, data, null)
        }

        @JvmStatic
        fun error(error: Throwable): RocketDetailsResponse {
            return RocketDetailsResponse(Status.ERROR, null, error)
        }
    }
}