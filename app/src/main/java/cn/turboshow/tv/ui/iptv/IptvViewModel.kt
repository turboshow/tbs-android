package cn.turboshow.tv.ui.iptv

import androidx.lifecycle.ViewModel
import cn.turboshow.tv.data.PlaylistRepository
import cn.turboshow.tv.data.SettingsRepository
import javax.inject.Inject

class IptvViewModel @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    settingsRepository: SettingsRepository
): ViewModel() {
    val channels = playlistRepository.channels
    val currentChannel = playlistRepository.currentChannel
    val udpxyAddr = settingsRepository.udpxyAddr

    fun selectChannel(index: Int) {
        playlistRepository.selectChannel(index)
    }

    fun nextChannel() {
        playlistRepository.nextChannel()
    }

    fun prevChannel() {
        playlistRepository.prevChannel()
    }
}