package cn.turboshow.tv.ui.player

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.turboshow.tv.player.TBSPlayer
import org.videolan.libvlc.util.VLCVideoLayout
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
    private val context: Context
) : ViewModel() {
    private val player = TBSPlayer(context)

    val title = MutableLiveData<String>()

    fun attachViews(videoLayout: VLCVideoLayout) {
        player.attachViews(videoLayout)
    }

    fun play(uri: String) {
        player.play(Uri.parse(uri))
    }

    fun stop() {
        player.stop()
    }

    fun pause() {
        player.pause()
    }

    fun resume() {
        player.play()
    }

    override fun onCleared() {
        player.release()

        super.onCleared()
    }
}