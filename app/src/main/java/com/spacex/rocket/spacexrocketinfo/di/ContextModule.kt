package com.spacex.rocket.spacexrocketinfo.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class ContextModule(private val context: Context) {
    @Provides
    @ApplicationScope
    @ApplicationContext
    fun provideContext(): Context {
        return context
    }
}