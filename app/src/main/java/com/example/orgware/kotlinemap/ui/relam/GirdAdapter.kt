package com.example.orgware.kotlinemap.ui.relam

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.orgware.kotlinemap.R
import com.example.orgware.kotlinemap.respones.gird.GirdResponse
import com.example.orgware.kotlinemap.respones.gird.ServicesItem
import kotlinx.android.synthetic.main.item_gird.view.*

class GirdAdapter(val context: Context, clickManager: ClickManager) : RecyclerView.Adapter<GirdAdapter.ViewHolder>() {

    var servicesItem: List<ServicesItem>? = null
    var clickManager: ClickManager? = null

    init {
        servicesItem = ArrayList()
    }

    fun setAdapterList(itemMain: ArrayList<ServicesItem>?) {
        if (itemMain == null) {
            return
        }
        servicesItem = itemMain
        notifyDataSetChanged()
    }

    interface ClickManager {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_gird, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return servicesItem!!.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(servicesItem!![position])

    }

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var tvtitle: TextView = v.findViewById(R.id.textView2)
        var img: ImageView = v.findViewById(R.id.imageView2)

        fun bindView(item: ServicesItem) {
            tvtitle.text=item.type
            Glide.with(context)
                    .load(item.servicesLogo)
                    .into(img)

        }
    }
}