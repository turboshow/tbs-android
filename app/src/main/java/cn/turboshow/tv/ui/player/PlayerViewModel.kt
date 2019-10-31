package cn.turboshow.tv.ui.player

import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.turboshow.tv.player.TBSPlayer
import org.videolan.libvlc.util.VLCVideoLayout
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PlayerViewModel @Inject constructor(
    private val context: Context
) : ViewModel() {
    private val player = TBSPlayer(context)

    val title = MutableLiveData<String>()
    val duration = MutableLiveData<String>()
    val position = MutableLiveData<String>()

    init {
        player.setCallback(object : TBSPlayer.Callback {
            override fun onDurationReady() {
                duration.postValue(formatTime(player.getDuration()))
            }

            override fun onPositionChanged() {
                position.postValue(formatTime(player.getPosition()))
            }

            override fun onCompleted() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun formatTime(millis: Long): String {
        return String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(millis),
            TimeUnit.MILLISECONDS.toMinutes(millis) -
                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
            TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)))
    }

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

    fun isPlaying(): Boolean {
        return player.isPlaying()
    }

    fun resume() {
        player.play()
    }

    override fun onCleared() {
        player.release()

        super.onCleared()
    }
}