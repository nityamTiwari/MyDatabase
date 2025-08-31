package com.ferrytech.camerasessionsapp.ui.adapters

import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.ferrytech.camerasessionsapp.R
import java.io.File

class PhotoAdapter(private val items: List<File>) :
    RecyclerView.Adapter<PhotoAdapter.VH>() {

    inner class VH(val imageView: ImageView) : RecyclerView.ViewHolder(imageView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val imageView = ImageView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                400
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return VH(imageView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val file = items[position]

        holder.imageView.load(file) {
            placeholder(R.drawable.ic_launcher_background) // 🟡 loading placeholder (add this in your drawable folder)
            error(R.drawable.ic_launcher_background)             // 🔴 error image if file fails to load
            crossfade(true)
            transformations(RoundedCornersTransformation(12f)) // Optional
        }
    }

    override fun getItemCount(): Int = items.size
}
