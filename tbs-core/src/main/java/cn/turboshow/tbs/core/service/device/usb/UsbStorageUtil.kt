package cn.turboshow.tbs.core.service.device.usb

import android.content.Context
import android.os.Environment
import cn.turboshow.tbs.core.util.FileUtils
import cn.turboshow.tbs.core.util.containsName
import cn.turboshow.tbs.core.util.startsWith
import org.videolan.libvlc.util.AndroidUtil
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

object UsbStorageUtil {
    private val EXTERNAL_PUBLIC_DIRECTORY: String = Environment.getExternalStorageDirectory().path

    private val typeWL =
        Arrays.asList("vfat", "exfat", "sdcardfs", "fuse", "ntfs", "fat32", "ext3", "ext4", "esdfs")
    private val typeBL = listOf("tmpfs")
    private val mountWL = arrayOf("/mnt", "/Removable", "/storage")
    private val mountBL = arrayOf(
        EXTERNAL_PUBLIC_DIRECTORY,
        "/mnt/secure",
        "/mnt/shell",
        "/mnt/asec",
        "/mnt/nand",
        "/mnt/runtime",
        "/mnt/obb",
        "/mnt/media_rw/extSdCard",
        "/mnt/media_rw/sdcard",
        "/storage/emulated",
        "/var/run/arc"
    )
    private val deviceWL = arrayOf("/dev/block/vold", "/dev/fuse", "/mnt/media_rw", "passthrough")

    fun listUsbDevices(context: Context): List<UsbStorageDevice> {
        var bufReader: BufferedReader? = null
        val mountPoints = ArrayList<String>()
        try {
            bufReader = BufferedReader(FileReader("/proc/mounts"))
            var line = bufReader.readLine()
            while (line != null) {
                val tokens = StringTokenizer(line, " ")
                val device = tokens.nextToken()
                val mountPoint = tokens.nextToken().replace("\\\\040".toRegex(), " ")
                val type = if (tokens.hasMoreTokens()) tokens.nextToken() else null
                if (mountPoints.contains(mountPoint) || typeBL.contains(type) || startsWith(
                        mountBL,
                        mountPoint
                    )
                ) {
                    line = bufReader.readLine()
                    continue
                }
                if (startsWith(deviceWL, device) && (typeWL.contains(type) || startsWith(
                        mountWL,
                        mountPoint
                    ))
                ) {
                    val position =
                        containsName(mountPoints, FileUtils.getFileNameFromPath(mountPoint))
                    if (position > -1) mountPoints.removeAt(position)
                    mountPoints.add(mountPoint)
                }
                line = bufReader.readLine()
            }
        } catch (ignored: IOException) {
        } finally {
            bufReader?.close()
        }
        mountPoints.remove(EXTERNAL_PUBLIC_DIRECTORY)

        return mountPoints.map {
            UsbStorageDevice(getVolumeTitle(FileUtils.getFileNameFromPath(it), context)!!, it)
        }
    }

    fun getVolumeTitle(uuid: String, context: Context): String? {
        if (!AndroidUtil.isMarshMallowOrLater) return null
        var volumeDescription: String? = null
        try {
            val storageManager = context.getSystemService(Context.STORAGE_SERVICE)
            val classType = storageManager.javaClass
            val findVolumeByUuid = classType.getDeclaredMethod("findVolumeByUuid", uuid.javaClass)
            findVolumeByUuid.isAccessible = true
            val volumeInfo = findVolumeByUuid.invoke(storageManager, uuid)
            val volumeInfoClass = Class.forName("android.os.storage.VolumeInfo")
            val getBestVolumeDescription =
                classType.getDeclaredMethod("getBestVolumeDescription", volumeInfoClass)
            getBestVolumeDescription.isAccessible = true
            volumeDescription =
                getBestVolumeDescription.invoke(storageManager, volumeInfo) as String
        } catch (ignored: Throwable) {
        }

        return volumeDescription
    }
}