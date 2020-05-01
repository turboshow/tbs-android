package cn.turboshow.tbs.core.service.device

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import cn.turboshow.tbs.core.dlna.DlnaManager
import cn.turboshow.tbs.core.service.device.usb.UsbStorageManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeviceService(context: Context, private val lifecycleOwner: LifecycleOwner) {
    val devices = mutableListOf<Device>()

    private val onDeviceAddedListeners = mutableListOf<(Device) -> Unit>()
    private val onDeviceRemovedListeners = mutableListOf<(Device) -> Unit>()

    fun addOnDeviceAddedListener(listener: (Device) -> Unit) {
        onDeviceAddedListeners.add(listener)
    }

    fun addOnDeviceRemovedListener(listener: (Device) -> Unit) {
        onDeviceRemovedListeners.add(listener)
    }

    private val deviceManagers =
        listOf<DeviceManager>(DlnaManager(context), UsbStorageManager(context))

    fun findDevice(deviceRef: String): Device? {
        return devices.find {
            it.ref == deviceRef
        }
    }

    private fun onDeviceAdded(device: Device) {
        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            devices.add(device)
            onDeviceAddedListeners.forEach {
                it.invoke(device)
            }
        }
    }

    private fun onDeviceRemoved(device: Device) {
        lifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
            devices.remove(device)
            onDeviceRemovedListeners.forEach {
                it.invoke(device)
            }
        }
    }

    fun startMonitorDevices() {
        deviceManagers.forEach {
            it.onDeviceAddedListener = ::onDeviceAdded
            it.onDeviceRemovedListener = ::onDeviceRemoved
            it.startMonitorDevices()
        }
    }

    fun stopMonitorDevices() {
        deviceManagers.forEach {
            it.stopMonitorDevices()
            it.onDeviceAddedListener = null
            it.onDeviceRemovedListener = null
        }

        onDeviceAddedListeners.clear()
        onDeviceRemovedListeners.clear()

        devices.clear()
    }
}