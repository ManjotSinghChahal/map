package com.app.map.ui.locationListActivity

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.app.map.data.local.AppEntities
import android.annotation.SuppressLint
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.map.databinding.LocationListBinding

class LocationListAdapter(private val context: Context, private val callback: LocationCallback) :
    ListAdapter<AppEntities, LocationListAdapter.ViewHolder>(LocationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), context, callback)
    }

    class ViewHolder constructor(private val binding: LocationListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: AppEntities, context: Context, callback: LocationCallback) {
            binding.apply {
                city.text = item.city
                address.text = item.address
                distance.text = item.distance.toString()
                deleteIcon.setOnClickListener {
                    callback.onLocationDeleted(item)
                }
                editIcon.setOnClickListener {
                    callback.onLocationEdit(item)
                }
                }
            }
        }

    private fun from(parent: ViewGroup): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LocationListBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    class LocationDiffCallback : DiffUtil.ItemCallback<AppEntities>() {
        override fun areItemsTheSame(oldItem: AppEntities, newItem: AppEntities): Boolean {
            return oldItem == newItem
        }
        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: AppEntities, newItem: AppEntities): Boolean {
            return oldItem == newItem
        }
    }

    interface LocationCallback {
        fun onLocationEdit(item: AppEntities)
        fun onLocationDeleted(item: AppEntities)
    }
}
