package cn.turboshow.tbs.core.service.dlna

import org.fourthline.cling.model.meta.Service

class MediaRenderer(private val service: Service<*, *>) {
    val name: String
        get() = service.device.details.friendlyName

    override fun equals(other: Any?): Boolean {
        return service.reference == (other as? Service<*, *>)?.reference
    }

    override fun hashCode(): Int {
        return service.reference.hashCode()
    }
}