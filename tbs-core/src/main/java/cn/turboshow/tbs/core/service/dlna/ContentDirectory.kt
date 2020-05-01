package cn.turboshow.tbs.core.service.dlna

import org.fourthline.cling.model.meta.Service

class ContentDirectory(private val service: Service<*, *>): DlnaEndPoint() {
    override val name: String
        get() = service.device.details.friendlyName

    override fun equals(other: Any?): Boolean {
        return service.reference == (other as? Service<*, *>)?.reference
    }

    override fun hashCode(): Int {
        return service.reference.hashCode()
    }
}