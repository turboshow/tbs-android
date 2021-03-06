package cn.turboshow.tv.ui.player

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.lifecycle.ViewModelProvider
import cn.turboshow.tv.R
import cn.turboshow.tv.di.DaggerFragmentActivity
import com.google.samples.apps.iosched.shared.util.viewModelProvider
import kotlinx.android.synthetic.main.activity_player.*
import javax.inject.Inject

class PlayerActivity : DaggerFragmentActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_player)

        viewModel = viewModelProvider(viewModelFactory)
        viewModel.run {
            title.value = intent.getStringExtra(ARG_TITLE)
            attachViews(videoView)
            play(intent.getStringExtra(ARG_URI))
        }
    }

    private fun showOverlay() {
        val overlayFragment = PlayerOverlayFragment.newInstance()
        overlayFragment.show(supportFragmentManager, "overlay")
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> {
                if (viewModel.isPlaying()) {
                    viewModel.pause()
                    showOverlay()
                }
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_URI = "uri"

        fun newIntent(context: Context, title: String, uri: String): Intent {
            return Intent(context, PlayerActivity::class.java).apply {
                putExtra(ARG_TITLE, title)
                putExtra(ARG_URI, uri)
            }
        }
    }
}