package cn.turboshow.tv.device.dlna

import android.content.Context
import cn.turboshow.tv.device.DeviceManager
import org.fourthline.cling.UpnpServiceImpl
import org.fourthline.cling.android.AndroidRouter
import org.fourthline.cling.android.AndroidUpnpServiceConfiguration
import org.fourthline.cling.model.meta.LocalDevice
import org.fourthline.cling.model.meta.RemoteDevice
import org.fourthline.cling.model.types.ServiceType
import org.fourthline.cling.protocol.ProtocolFactory
import org.fourthline.cling.registry.Registry
import org.fourthline.cling.registry.RegistryListener
import org.fourthline.cling.support.model.DIDLObject
import org.fourthline.cling.transport.Router

class DlnaManager(private val context: Context) : DeviceManager() {
    private val upnpService = object : UpnpServiceImpl(AndroidUpnpServiceConfiguration()) {
        override fun createRouter(protocolFactory: ProtocolFactory?, registry: Registry?): Router {
            return AndroidRouter(configuration, protocolFactory, context)
        }

        override fun shutdown() {
            (router as AndroidRouter).unregisterBroadcastReceiver()
            super.shutdown()
        }
    }

    private val upnpRegistryListener = object : RegistryListener {
        override fun localDeviceRemoved(registry: Registry?, device: LocalDevice?) {
        }

        override fun remoteDeviceDiscoveryStarted(registry: Registry?, device: RemoteDevice?) {
        }

        override fun remoteDeviceDiscoveryFailed(
            registry: Registry?,
            device: RemoteDevice?,
            ex: Exception?
        ) {
        }

        override fun afterShutdown() {
        }

        override fun remoteDeviceAdded(registry: Registry, device: RemoteDevice) {
            onClingDeviceAdded(device)
        }

        override fun remoteDeviceUpdated(registry: Registry?, device: RemoteDevice?) {
        }

        override fun beforeShutdown(registry: Registry?) {
        }

        override fun remoteDeviceRemoved(registry: Registry, device: RemoteDevice) {
            onClingDeviceRemoved(device)
        }

        override fun localDeviceAdded(registry: Registry?, device: LocalDevice?) {
        }

    }

    private fun onClingDeviceAdded(clingDevice: org.fourthline.cling.model.meta.Device<*, *, *>) {
        val contentDirectoryService = clingDevice.services.find {
            it.serviceType.type == UPNP_SERVICE_TYPE_CONTENT_DIRECTORY
        }

        if (contentDirectoryService != null) {
            val device = DlnaDevice(upnpService, clingDevice)
            onDeviceAdded(device)
        }
    }

    private fun onClingDeviceRemoved(clingDevice: org.fourthline.cling.model.meta.Device<*, *, *>) {
        val contentDirectoryService = clingDevice.services.find {
            it.serviceType.type == UPNP_SERVICE_TYPE_CONTENT_DIRECTORY
        }

        if (contentDirectoryService != null) {
            val device = DlnaDevice(upnpService, clingDevice)
            onDeviceAdded(device)
        }
    }

    override fun startMonitorDevices() {
        upnpService.registry.addListener(upnpRegistryListener)
        upnpService.controlPoint.search()
    }

    override fun stopMonitorDevices() {
        upnpService.registry.removeListener(upnpRegistryListener)
        upnpService.shutdown()
    }

    companion object {
        const val UPNP_SERVICE_TYPE_CONTENT_DIRECTORY = "ContentDirectory"
    }
}
