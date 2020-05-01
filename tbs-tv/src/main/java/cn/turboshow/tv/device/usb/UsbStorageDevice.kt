package cn.turboshow.tv.device.usb

import cn.turboshow.tv.device.Device
import cn.turboshow.tv.device.DeviceFile
import cn.turboshow.tv.util.FileUtils.isMediaFile
import java.io.File

class UsbStorageDevice(private val volumeTitle: String, private val path: String): Device() {
    override val name: String
        get() = volumeTitle

    override val ref: String
        get() = path

    override val rootDirectoryFile: DeviceFile
        get() = DeviceFile(name, path, true, false)

    override suspend fun listDirectory(file: DeviceFile): List<DeviceFile> {
        return File(file.uri).listFiles().map {
            DeviceFile(it.name, "file://${it.path}", false, isMediaFile(it.path))
        }
    }
}