package cn.turboshow.tv.ui

import android.os.Bundle
import android.os.IBinder
import androidx.leanback.app.BrowseSupportFragment
import androidx.leanback.widget.*
import cn.turboshow.tv.R
import cn.turboshow.tv.browse.UpnpDeviceItem
import cn.turboshow.tv.service.TBSService
import cn.turboshow.tv.ui.browse.BrowseActivity
import cn.turboshow.tv.ui.presenter.GridItemPresenter
import cn.turboshow.tv.util.ServiceBinder

class MainFragment : BrowseSupportFragment() {
    private var tbsService: TBSService? = null
    private val devicesAdapter = ArrayObjectAdapter(GridItemPresenter())

    private lateinit var serviceBinder: ServiceBinder

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupUIElements()
        setupEventListeners()
        loadData()
        initService()
    }

    private fun initService() {
        serviceBinder = object : ServiceBinder(activity!!, this) {
            override fun onServiceConnected(binder: IBinder) {
                tbsService = (binder as TBSService.Binder).getService().also { tbsService ->
                    tbsService.deviceRegistry.run {
                        devices.forEach {
                            devicesAdapter.add(UpnpDeviceItem(it))
                        }
                        addOnDeviceAddedListener {
                            devicesAdapter.add(UpnpDeviceItem(it))
                        }
                        addOnDeviceRemovedListener {
                            devicesAdapter.remove(UpnpDeviceItem(it))
                        }
                    }
                }
            }

            override fun onServiceDisconnected() {
                tbsService = null
            }
        }
        serviceBinder.bind(TBSService::class.java)
    }

    private fun loadData() {
        adapter = ArrayObjectAdapter(ListRowPresenter()).apply {
            add(
                ListRow(
                    HeaderItem(resources.getString(R.string.browse)), devicesAdapter
                )
            )
        }
    }

    private fun setupUIElements() {
        title = resources.getString(R.string.app_name)
        headersState = HEADERS_ENABLED
        isHeadersTransitionOnBackEnabled = true
        brandColor = resources.getColor(R.color.colorPrimary)
    }

    private fun setupEventListeners() {
        onItemViewClickedListener = OnItemViewClickedListener { _, item, _, _ ->
            when (item) {
                is UpnpDeviceItem -> {
                    context!!.startActivity(
                        BrowseActivity.newIntent(
                            context!!,
                            item.device
                        )
                    )
                }
            }
        }
    }
}