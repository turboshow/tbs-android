package cn.turboshow.tv.di

import androidx.lifecycle.LifecycleService
import dagger.android.AndroidInjection

abstract class DaggerLifecycleService: LifecycleService() {
    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }
}