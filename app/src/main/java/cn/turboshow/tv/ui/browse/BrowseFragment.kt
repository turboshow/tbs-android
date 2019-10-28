package cn.turboshow.tv.ui.browse

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.leanback.widget.OnItemViewClickedListener
import cn.turboshow.tv.R
import cn.turboshow.tv.device.DeviceFile
import cn.turboshow.tv.util.ServiceBinder
import kotlinx.android.synthetic.main.fragment_browse.view.*

class BrowseFragment : Fragment() {
    private val itemsAdapter = BrowserItemsAdapter()
    private lateinit var serviceBinder: ServiceBinder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val files = arguments!!.getParcelableArray(ARG_FILES) as Array<DeviceFile>
        files.forEach {
            itemsAdapter.add(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_browse, container, false).apply {
            titleView.text = arguments?.getString(ARG_TITLE)
            itemsView.run {
                setNumColumns(1)
                verticalSpacing = 0
                setBackgroundColor(Color.parseColor("#111111"))
                adapter = itemsAdapter
            }
        }
    }

    fun setOnItemViewClickedListener(listener: OnItemViewClickedListener) {
        itemsAdapter.onItemViewClickedListener = listener
    }

    companion object {
        private const val ARG_TITLE = "title"
        private const val ARG_FILES = "files"

        fun newInstance(
            title: String,
            files: List<DeviceFile>
        ): BrowseFragment {
            return BrowseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                    putParcelableArray(ARG_FILES, files.toTypedArray())
                }
            }
        }
    }
}
