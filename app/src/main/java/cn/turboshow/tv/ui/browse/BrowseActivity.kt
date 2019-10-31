@file:Suppress("DEPRECATION")

package cn.turboshow.tv.ui.browse

import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.FragmentActivity
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.lifecycle.lifecycleScope
import cn.turboshow.tv.R
import cn.turboshow.tv.device.Device
import cn.turboshow.tv.device.DeviceFile
import cn.turboshow.tv.service.TBSService
import cn.turboshow.tv.ui.player.PlayerActivity
import cn.turboshow.tv.util.ServiceBinder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BrowseActivity : FragmentActivity() {
    private lateinit var device: Device
    private lateinit var progressDialog: ProgressDialog
    private val mediaUnmountedReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                val mountPoint = it.data!!.path
                if (mountPoint == device.ref) {
                    finish()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_browse)

        progressDialog = ProgressDialog(this)

        bindService()

        registerReceiver(
            mediaUnmountedReceiver,
            IntentFilter(Intent.ACTION_MEDIA_UNMOUNTED).apply {
                addDataScheme("file")
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()

        unregisterReceiver(mediaUnmountedReceiver)
    }

    private fun bindService() {
        val serviceBinder = object : ServiceBinder(this, this) {
            override fun onServiceConnected(binder: IBinder) {
                val deviceRef = intent.getStringExtra(ARG_DEVICE_REF)
                val tbsService = (binder as TBSService.Binder).getService()
                val deviceManager = tbsService.deviceRegistry
                device = deviceManager.findDevice(deviceRef)!!

                showRootDirectory()
            }

            override fun onServiceDisconnected() {
            }

        }
        serviceBinder.bind(TBSService::class.java)
    }

    private fun showRootDirectory() {
        val rootDirectoryFile = device.rootDirectoryFile
        showDirectory(rootDirectoryFile, true)
    }

    private fun showDirectory(directoryFile: DeviceFile, isRoot: Boolean) {
        lifecycleScope.launch(Dispatchers.Main) {
            progressDialog.show()
            try {
                val files = device.listDirectory(directoryFile)
                progressDialog.dismiss()
                val fragment = BrowseFragment.newInstance(directoryFile.name, files)
                fragment.setOnItemViewClickedListener(OnItemViewClickedListener { _, file, _, _ ->
                    if (file is DeviceFile) {
                        if (file.isDirectory) {
                            showDirectory(file, false)
                        } else if (file.isMedia) {
                            playFile(file)
                        }
                    }
                })
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, fragment)
                    if (!isRoot) {
                        addToBackStack(null)
                    }
                }.commit()
            } catch (e: Exception) {
                progressDialog.dismiss()
                Toast.makeText(this@BrowseActivity, e.message, LENGTH_SHORT).show()
            }
        }
    }

    private fun playFile(file: DeviceFile) {
        val title = file.name
        val url = file.uri
        startActivity(PlayerActivity.newIntent(this, title, url))
    }

    companion object {
        private const val ARG_DEVICE_REF = "device_ref"

        fun newIntent(context: Context, device: Device): Intent {
            return Intent(context, BrowseActivity::class.java).apply {
                putExtra(ARG_DEVICE_REF, device.ref)
            }
        }
    }
}
