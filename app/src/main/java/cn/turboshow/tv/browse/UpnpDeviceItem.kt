package cn.turboshow.tv.browse

import android.graphics.drawable.ColorDrawable
import cn.turboshow.tv.device.Device

class UpnpDeviceItem(val device: Device) :
    BrowseItem(ColorDrawable(), device.name) {

    override fun equals(other: Any?): Boolean {
        return device == (other as UpnpDeviceItem?)?.device
    }

    override fun hashCode(): Int {
        return device.hashCode()
    }
}