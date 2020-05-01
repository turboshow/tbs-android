package cn.turboshow.tbs.core.service.dlna

import android.content.Context
import androidx.lifecycle.MutableLiveData
import org.fourthline.cling.UpnpServiceImpl
import org.fourthline.cling.android.AndroidRouter
import org.fourthline.cling.android.AndroidUpnpServiceConfiguration
import org.fourthline.cling.model.meta.LocalDevice
import org.fourthline.cling.model.meta.RemoteDevice
import org.fourthline.cling.protocol.ProtocolFactory
import org.fourthline.cling.registry.Registry
import org.fourthline.cling.registry.RegistryListener
import org.fourthline.cling.transport.Router

class DlnaService(private val context: Context) {
    val mediaRenderers = MutableLiveData<List<MediaRenderer>>().apply { value = listOf() }
    val contentDirectories = MutableLiveData<List<ContentDirectory>>().apply { value = listOf() }

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
            onDeviceAdded(device)
        }

        override fun remoteDeviceUpdated(registry: Registry?, device: RemoteDevice?) {
        }

        override fun beforeShutdown(registry: Registry?) {
        }

        override fun remoteDeviceRemoved(registry: Registry, device: RemoteDevice) {
            onDeviceRemoved(device)
        }

        override fun localDeviceAdded(registry: Registry?, device: LocalDevice?) {
        }

    }

    fun start() {
        upnpService.registry.addListener(upnpRegistryListener)
        upnpService.controlPoint.search()
    }

    fun stop() {
        upnpService.registry.removeListener(upnpRegistryListener)
        upnpService.shutdown()
    }

    // TODO: Synchronization
    private fun onDeviceAdded(device: org.fourthline.cling.model.meta.Device<*, *, *>) {
        device.services.forEach {
            if (it.serviceType.type == UPNP_SERVICE_TYPE_MEDIA_RENDERER) {
                mediaRenderers.postValue(mediaRenderers.value!! + MediaRenderer(it))
            } else if (it.serviceType.type == UPNP_SERVICE_TYPE_CONTENT_DIRECTORY) {
                contentDirectories.postValue(contentDirectories.value!! + ContentDirectory(it))
            }
        }
    }

    // TODO: Synchronization
    private fun onDeviceRemoved(device: org.fourthline.cling.model.meta.Device<*, *, *>) {
        device.services.forEach {
            if (it.serviceType.type == UPNP_SERVICE_TYPE_MEDIA_RENDERER) {
                mediaRenderers.postValue(mediaRenderers.value!! - MediaRenderer(it))
            } else if (it.serviceType.type == UPNP_SERVICE_TYPE_CONTENT_DIRECTORY) {
                contentDirectories.postValue(contentDirectories.value!! - ContentDirectory(it))
            }
        }
    }

    companion object {
        const val UPNP_SERVICE_TYPE_CONTENT_DIRECTORY = "ContentDirectory"
        const val UPNP_SERVICE_TYPE_MEDIA_RENDERER = "MediaRenderer"
    }
}