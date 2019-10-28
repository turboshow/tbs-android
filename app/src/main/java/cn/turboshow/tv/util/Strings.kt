@file:JvmName("Strings")
package cn.turboshow.tv.util


import cn.turboshow.tv.BuildConfig
import java.text.DecimalFormat

private const val TAG = "VLC/UiTools/Strings"

fun stripTrailingSlash(s: String): String {
    return if (s.endsWith("/") && s.length > 1) s.substring(0, s.length - 1) else s
}

//TODO: Remove this after convert the dependent code to kotlin
fun startsWith(array: Array<String>, text: String) = array.any { text.startsWith(it)}

//TODO: Remove this after convert the dependent code to kotlin
fun containsName(list: List<String>, text: String) = list.indexOfLast { it.endsWith(text) }

/**
 * Get the formatted current playback speed in the form of 1.00x
 */
fun Float.formatRateString() = String.format(java.util.Locale.US, "%.2fx", this)

fun Long.readableFileSize(): String {
    val size: Long = this
    if (size <= 0) return "0"
    val units = arrayOf("B", "KiB", "MiB", "GiB", "TiB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
}

fun Long.readableSize(): String {
    val size: Long = this
    if (size <= 0) return "0"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1000.0)).toInt()
    return DecimalFormat("#,##0.#").format(size / Math.pow(1000.0, digitGroups.toDouble())) + " " + units[digitGroups]
}

fun String.removeFileProtocole(): String {
    return if (this.startsWith("file://"))
        this.substring(7)
    else
        this
}

fun String.buildPkgString() = "${BuildConfig.APPLICATION_ID}.$this"
