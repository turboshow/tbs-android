package cn.turboshow.tbs.core.util

import android.content.Context
import android.content.Context.STORAGE_SERVICE
import org.videolan.libvlc.util.AndroidUtil

object FileUtils {
    fun getFileNameFromPath(path: String?, withExtension: Boolean = true): String {
        var path: String? = path ?: return ""
        var index = path!!.lastIndexOf('/')
        if (index == path.length - 1) {
            path = path.substring(0, index)
            index = path.lastIndexOf('/')
        }
        val fileName = if (index > -1)
            path.substring(index + 1)
        else
            path

        if (withExtension) return fileName

        val end = fileName.lastIndexOf(".")
        return if (end <= 0) fileName else fileName.substring(0, end)
    }

    fun isMediaFile(path: String): Boolean {
        val lastDotIndex = path.lastIndexOf(".")
        if (lastDotIndex == -1) return false

        val extension = path.substring(lastDotIndex + 1)

        return extension in listOf("mp4", "mkv")
    }
}

