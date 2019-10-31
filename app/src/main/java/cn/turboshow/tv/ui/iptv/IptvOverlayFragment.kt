package cn.turboshow.tv.ui.iptv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import cn.turboshow.tv.R
import com.google.samples.apps.iosched.shared.util.activityViewModelProvider
import dagger.android.support.DaggerDialogFragment
import kotlinx.android.synthetic.main.fragment_channels.*
import javax.inject.Inject

class IptvOverlayFragment : DaggerDialogFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: IptvViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activityViewModelProvider(viewModelFactory)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_channels, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initChannelsView()
    }

    private fun initChannelsView() {
        viewModel.channels.observe(this, androidx.lifecycle.Observer {
            channelsView.adapter = ArrayAdapter(context!!, android.R.layout.simple_list_item_1, it)
            channelsView.setOnItemClickListener { _, _, position, _ ->
                viewModel.selectChannel(position)
                dismiss()
            }
            channelsView.post { channelsView.setSelection(it.indexOf(viewModel.currentChannel.value)) }
        })
    }

    companion object {
        fun newInstance() = IptvOverlayFragment()
    }
}
