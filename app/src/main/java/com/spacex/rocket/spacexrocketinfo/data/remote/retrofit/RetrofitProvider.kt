package com.spacex.rocket.spacexrocketinfo.data.remote.retrofit

import android.content.Context
import android.net.ConnectivityManager
import com.google.gson.GsonBuilder
import com.spacex.rocket.spacexrocketinfo.di.ApplicationContext
import com.spacex.rocket.spacexrocketinfo.utils.Constants.BASE_URL
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class RetrofitProvider @Inject constructor(@ApplicationContext val context: Context) {

    fun getRetrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient
            .Builder()
            .cache(getCache())
            .addInterceptor(offlineInterceptor())
            .addNetworkInterceptor(onlineInterceptor())
            .addInterceptor(interceptor)
            .build()

        val gson = GsonBuilder()
            .create()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    private fun getCache(): Cache {
        val cacheSize = 10 * 1024 // 10 MiB
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    private fun onlineInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val maxAge = 60
            response.newBuilder()
                .header("Cache-Control", "public, max-age=$maxAge")
                .removeHeader("Pragma")
                .build()
        }
    }

    private fun offlineInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!isOnline()) {
                val maxStale = 60 * 60 * 24 * 30 // Offline cache available for 30 days
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader("Pragma")
                    .build()
            }
            chain.proceed(request)
        }
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