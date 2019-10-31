package cn.turboshow.tv.ui.player

import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import cn.turboshow.tv.R
import cn.turboshow.tv.databinding.FragmentPlayerOverlayBinding
import com.google.samples.apps.iosched.shared.util.activityViewModelProvider
import dagger.android.support.DaggerDialogFragment
import javax.inject.Inject

class PlayerOverlayFragment : DaggerDialogFragment(), DialogInterface.OnKeyListener {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playerViewModel = activityViewModelProvider(viewModelFactory)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentPlayerOverlayBinding.inflate(inflater, container, false).apply {
            viewModel = playerViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.setOnKeyListener(this)
    }

    override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
        if (event?.action == KeyEvent.ACTION_UP) {
            when (keyCode) {
                KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_DPAD_CENTER -> {
                    playerViewModel.resume()
                    dismiss()
                    return true
                }
            }
        }
        return false
    }

    companion object {
        fun newInstance(): PlayerOverlayFragment {
            return PlayerOverlayFragment()
        }
    }
}