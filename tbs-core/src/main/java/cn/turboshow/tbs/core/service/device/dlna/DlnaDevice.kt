package cn.turboshow.tbs.core.service.dlna

import cn.turboshow.tbs.core.dlna.DlnaManager.Companion.UPNP_SERVICE_TYPE_CONTENT_DIRECTORY
import cn.turboshow.tbs.core.service.device.Device
import cn.turboshow.tbs.core.service.device.DeviceFile
import org.fourthline.cling.UpnpService
import org.fourthline.cling.model.action.ActionInvocation
import org.fourthline.cling.model.message.UpnpResponse
import org.fourthline.cling.model.meta.Service
import org.fourthline.cling.support.contentdirectory.callback.Browse
import org.fourthline.cling.support.model.BrowseFlag
import org.fourthline.cling.support.model.DIDLContent
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val ROOT_CONTAINER_ID = "0"

class DlnaDevice(
    private val upnpService: UpnpService,
    private val clingDevice: org.fourthline.cling.model.meta.Device<*, *, *>
) : Device() {
    private val contentDirectoryService =
        clingDevice.services.find {
            it.serviceType.type == UPNP_SERVICE_TYPE_CONTENT_DIRECTORY
        }

    override val name: String
        get() = clingDevice.displayString

    override val ref: String
        get() = clingDevice.identity.udn.identifierString

    override val rootDirectoryFile = DeviceFile("", ROOT_CONTAINER_ID, true, false)

    override suspend fun listDirectory(file: DeviceFile): List<DeviceFile> {
        return suspendCoroutine { continuation ->
            upnpService.controlPoint.execute(object :
                Browse(contentDirectoryService, file.uri, BrowseFlag.DIRECT_CHILDREN) {
                override fun updateStatus(status: Status?) {
                }

                override fun received(
                    actionInvocation: ActionInvocation<out Service<*, *>>?,
                    didl: DIDLContent
                ) {
                    val items = mutableListOf<DeviceFile>()
                    items += didl.containers.map {
                        DeviceFile(it.title, it.id, true, false)
                    }
                    items += didl.items.map {
                        DeviceFile(it.title, it.firstResource.value, false, true)
                    }
                    continuation.resume(items)
                }

                override fun failure(
                    invocation: ActionInvocation<out Service<*, *>>?,
                    operation: UpnpResponse?,
                    defaultMsg: String?
                ) {
                    continuation.resumeWith(Result.failure(Exception(defaultMsg)))
                }

            })
        }
    }

    override fun equals(other: Any?): Boolean {
        return clingDevice == (other as DlnaDevice?)?.clingDevice
    }

    override fun hashCode(): Int {
        return clingDevice.hashCode()
    }
}