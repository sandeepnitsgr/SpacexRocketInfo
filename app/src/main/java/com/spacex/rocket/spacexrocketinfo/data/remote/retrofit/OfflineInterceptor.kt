package com.spacex.rocket.spacexrocketinfo.data.remote.retrofit

import android.content.Context
import android.net.ConnectivityManager
import com.spacex.rocket.spacexrocketinfo.di.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class OfflineInterceptor @Inject constructor(@ApplicationContext val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if (!isOnline()) {
            val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
            request = request.newBuilder()
                .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                .removeHeader("Pragma")
                .build()
        }
        return chain.proceed(request)
    }

    private fun isOnline(): Boolean {
        val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfo = manager!!.activeNetworkInfo
        var isAvailable = false
        if (networkInfo != null && networkInfo.isConnected) {
            isAvailable = true
        }
        return isAvailable

    }
}
