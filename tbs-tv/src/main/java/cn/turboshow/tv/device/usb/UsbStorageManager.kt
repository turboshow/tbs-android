package cn.turboshow.tv.device.usb

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import cn.turboshow.tv.device.DeviceManager
import cn.turboshow.tv.device.usb.UsbStorageUtil.getVolumeTitle
import cn.turboshow.tv.device.usb.UsbStorageUtil.listUsbDevices
import cn.turboshow.tv.util.FileUtils.getFileNameFromPath

class UsbStorageManager(private val context: Context) : DeviceManager() {
    private val mediaMountedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val volumeTitle = getVolumeTitle(getFileNameFromPath(it.data?.path!!), context!!)!!
                onDeviceAdded(UsbStorageDevice(volumeTitle, it.data?.path!!))
            }
        }
    }

    private val mediaUnmountedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                onDeviceRemoved(UsbStorageDevice("", it.data?.path!!))
            }
        }
    }

    override fun startMonitorDevices() {
        listUsbDevices(context).forEach(::onDeviceAdded)

        context.registerReceiver(
            mediaMountedReceiver,
            IntentFilter(Intent.ACTION_MEDIA_MOUNTED).apply {
                addDataScheme("file")
            }
        )
        context.registerReceiver(
            mediaUnmountedReceiver,
            IntentFilter(Intent.ACTION_MEDIA_UNMOUNTED).apply {
                addDataScheme("file")
            }
        )
    }

    override fun stopMonitorDevices() {
        context.unregisterReceiver(mediaMountedReceiver)
        context.unregisterReceiver(mediaUnmountedReceiver)
    }
}