package com.spacex.rocket.spacexrocketinfo.data.remote.retrofit

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class OnlineInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val maxAge = 60
        return response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .removeHeader("Pragma")
            .build()
    }
}