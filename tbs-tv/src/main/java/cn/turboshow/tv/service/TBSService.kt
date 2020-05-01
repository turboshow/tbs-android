package cn.turboshow.tv.service

import android.content.Context
import android.content.Intent
import android.os.IBinder
import cn.turboshow.tv.device.DeviceRegistry
import cn.turboshow.tv.di.DaggerLifecycleService

class TBSService : DaggerLifecycleService() {
    lateinit var deviceRegistry: DeviceRegistry
    private val binder = Binder()

    override fun onBind(intent: Intent): IBinder {
        super.onBind(intent)

        return binder
    }

    override fun onCreate() {
        super.onCreate()

        deviceRegistry = DeviceRegistry(this, this)
        deviceRegistry.startMonitorDevices()
    }

    override fun onDestroy() {
        deviceRegistry.stopMonitorDevices()

        super.onDestroy()
    }

    inner class Binder : android.os.Binder() {
        fun getService(): TBSService {
            return this@TBSService
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, TBSService::class.java)
        }
    }
}
