package cn.turboshow.tv.device

abstract class Device {
    abstract val name: String
    abstract val ref: String
    abstract val rootDirectoryFile: DeviceFile

    abstract suspend fun listDirectory(
        file: DeviceFile
    ): List<DeviceFile>

    override fun equals(other: Any?): Boolean {
        return ref == (other as Device?)?.ref
    }

    override fun hashCode(): Int {
        return ref.hashCode()
    }
}

