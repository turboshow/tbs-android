package cn.turboshow.tv.device

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeviceFile(
    val name: String,
    val uri: String,
    val isDirectory: Boolean,
    val isMedia: Boolean
) : Parcelable