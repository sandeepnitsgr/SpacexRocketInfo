package com.spacex.rocket.spacexrocketinfo.data.model

class Response private constructor(val status: Status, val data: RocketListData?, val error: Throwable?) {
    companion object {
        @JvmStatic
        internal fun loading(): Response {
            return Response(Status.LOADING, null, null)
        }

        @JvmStatic
        fun success(data: RocketListData?): Response {
            return Response(Status.SUCCESS, data, null)
        }

        @JvmStatic
        fun error(error: Throwable): Response {
            return Response(Status.ERROR, null, error)
        }
    }
}