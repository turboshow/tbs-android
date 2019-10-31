package cn.turboshow.tv.player

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.SurfaceView
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.MediaPlayer.Event.*
import org.videolan.libvlc.util.VLCVideoLayout

class TBSPlayer(context: Context) {
    private val options = arrayListOf("-vv", "--vout=android_display,none")
    private val libVLC = LibVLC(context, options)
    private val player = MediaPlayer(libVLC)
    private var videoView: SurfaceView? = null
    private var prepared = false
    private var paused = false
    private var startPosition: Long = 0

    private var callback: Callback? = null

    interface Callback {
        fun onDurationReady()
        fun onPositionChanged()
        fun onCompleted()
    }

    fun setCallback(callback: Callback) {
        this.callback = callback
    }

    init {
        player.setEventListener {
            val eventType = when (it.type) {
                MediaChanged -> "MediaChanged"
                Opening -> {
                    if (startPosition > 0) {
                        player.time = startPosition
                    }
                    "Opening"
                }
                Buffering -> "Buffering"
                Playing -> "Playing"
                Paused -> "Paused"
                Stopped -> "Stopped"
                EndReached -> {
                    callback?.onCompleted()
                    "EndReached"
                }
                EncounteredError -> "EncounteredError"
                TimeChanged -> "TimeChanged"
                PositionChanged -> {
                    callback?.onPositionChanged()
                    "positionChanged"
                }
                SeekableChanged -> "SeekableChanged"
                PausableChanged -> "PausableChanged"
                LengthChanged -> {
                    callback?.onDurationReady()
                    "LengthChanged"
                }
                Vout -> "Vout"
                ESAdded -> "ESAdded"
                ESDeleted -> "ESDeleted"
                ESSelected -> "ESSelected"
                else -> it.type.toString(16)
            }

            Log.d("VLC Event >>>", eventType)
        }
    }

    fun setVideoView(videoView: SurfaceView) {
        this.videoView = videoView
    }

    fun attachViews(videoLayout: VLCVideoLayout) {
        player.attachViews(videoLayout, null, true, false)
    }

    fun setMedia(uri: Uri) {
        player.media = Media(libVLC, uri)
    }

    fun setStartPosition(position: Long) {
        startPosition = position
    }

    fun isPrepared(): Boolean {
        return prepared
    }

    fun play() {
        if (!paused) {
            stop()
        }
        player.play()
        paused = false
    }

    fun play(uri: Uri) {
        setMedia(uri)
        play()
    }

    fun isPlaying(): Boolean {
        return player.isPlaying
    }

    fun pause() {
        player.pause()
        paused = true
    }

    fun getDuration(): Long {
        return player.length
    }

    fun getPosition(): Long {
        return player.time
    }

    fun seekTo(position: Long) {
        player.time = position
    }

    fun stop() {
        player.stop()
    }

    fun release() {
        callback = null
        player.release()
        libVLC.release()
    }
}