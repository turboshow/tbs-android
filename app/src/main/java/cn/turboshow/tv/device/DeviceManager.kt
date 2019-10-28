package cn.turboshow.tv.device

abstract class DeviceManager() {
    var onDeviceAddedListener: ((Device) -> Unit)? = null
    var onDeviceRemovedListener: ((Device) -> Unit)? = null

    fun onDeviceAdded(device: Device) {
        onDeviceAddedListener?.invoke(device)
    }

    fun onDeviceRemoved(device: Device) {
        onDeviceRemovedListener?.invoke(device)
    }

    abstract fun startMonitorDevices()
    abstract fun stopMonitorDevices()
}