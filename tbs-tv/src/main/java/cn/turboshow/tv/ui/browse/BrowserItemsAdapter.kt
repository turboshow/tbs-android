package cn.turboshow.tv.ui.browse

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.leanback.widget.OnItemViewClickedListener
import androidx.recyclerview.widget.RecyclerView
import cn.turboshow.tv.R
import cn.turboshow.tv.device.DeviceFile

class BrowserItemsAdapter() :
    RecyclerView.Adapter<BrowserItemsAdapter.ViewHolder>() {
    private val items = mutableListOf<DeviceFile>()
    var onItemViewClickedListener: OnItemViewClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_browse, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val item = items[position]
        holder.setIcon(
            if (item.isDirectory)
                context.getDrawable(R.drawable.ic_folder)!!
            else
                context.getDrawable(R.drawable.ic_file)!!
        )
        holder.setTitle(item.name)
        holder.itemView.setOnClickListener {
            if (onItemViewClickedListener != null) {
                onItemViewClickedListener!!.onItemClicked(null, item, null, null)
            }
        }
    }

    fun add(item: DeviceFile) {
        items.add(item)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val iconView: ImageView = itemView.findViewById(R.id.iconView)
        private val titleView: TextView = itemView.findViewById(R.id.titleView)

        fun setIcon(icon: Drawable) {
            iconView.setImageDrawable(icon)
        }

        fun setTitle(title: String) {
            titleView.text = title
        }
    }
}