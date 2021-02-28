package com.spacex.rocket.spacexrocketinfo.di

import android.content.Context
import com.spacex.rocket.spacexrocketinfo.data.remote.retrofit.ApiService
import com.spacex.rocket.spacexrocketinfo.data.remote.retrofit.OfflineInterceptor
import com.spacex.rocket.spacexrocketinfo.data.remote.retrofit.OnlineInterceptor
import com.spacex.rocket.spacexrocketinfo.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
class RetrofitModule constructor(
    private var context: Context
) {
    @Provides
    @ApplicationScope
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @ApplicationScope
    fun getOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        cache: Cache,
        onlineInterceptor: OnlineInterceptor,
        offlineInterceptor: OfflineInterceptor
    ): OkHttpClient {
        return OkHttpClient
            .Builder()
            .cache(cache)
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(offlineInterceptor)
            .addNetworkInterceptor(onlineInterceptor)
            .build()
    }

    @ApplicationScope
    @Provides
    fun provideOfflineInterceptor(context: Context): Interceptor {
        return OfflineInterceptor(context)
    }

    @ApplicationScope
    @Provides
    fun provideOnlineInterceptor(): Interceptor {
        return OnlineInterceptor()
    }

    @ApplicationScope
    @Provides
    fun getOkhttpClientCache(): Cache {
        return Cache(context.cacheDir, (10 * 1024).toLong())
    }

    @ApplicationScope
    @Provides
    fun getHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return httpLoggingInterceptor
    }
    @Provides
    @ApplicationScope
    fun getApiInterface(retroFit: Retrofit): ApiService {
        return retroFit.create(ApiService::class.java)
    }
}