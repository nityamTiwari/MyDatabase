package com.ferrytech.camerasessionsapp.ui.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ferrytech.camerasessionsapp.R
import com.ferrytech.camerasessionsapp.data.model.Session
class SessionAdapter(private val onClick: (Session) -> Unit) :
    ListAdapter<Session, SessionAdapter.VH>(DIFF) {
    companion object {
        val DIFF = object : DiffUtil.ItemCallback<Session>() {
            override fun areItemsTheSame(oldItem: Session, newItem: Session) =
                oldItem.sessionId == newItem.sessionId
            override fun areContentsTheSame(oldItem: Session, newItem: Session)
                    = oldItem == newItem
        }
    }
    inner class VH(view: View) : RecyclerView.ViewHolder(view) {

        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvMeta: TextView = view.findViewById(R.id.tvMeta)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent,
                false)
        return VH(v)
    }
    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        holder.tvName.text = item.name
        holder.tvMeta.text = "Age: ${item.age} • ID: ${item.sessionId}"
        holder.itemView.setOnClickListener { onClick(item) }
    }
}