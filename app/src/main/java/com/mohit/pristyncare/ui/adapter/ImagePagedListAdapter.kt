package com.mohit.pristyncare.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mohit.pristyncare.R
import com.mohit.pristyncare.data.repository.NetworkState
import com.mohit.pristyncare.model.Photo
import com.mohit.pristyncare.ui.activity.ViewActivity
import kotlinx.android.synthetic.main.item_image.view.*
import kotlinx.android.synthetic.main.item_network_state.view.*

class ImagePagedListAdapter(private val context: Context) :
    PagedListAdapter<Photo, RecyclerView.ViewHolder>(ImageDiffCallback()) {

    val IMAGE_VIEW_TYPE = 1
    val NETWORK_VIEW_TYPE = 2

    private var networkState: NetworkState? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: View

        return if (viewType == IMAGE_VIEW_TYPE) {
            view = layoutInflater.inflate(R.layout.item_image, parent, false)
            ImageItemViewHolder(view)
        } else {
            view = layoutInflater.inflate(R.layout.item_network_state, parent, false)
            NetworkStateItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == IMAGE_VIEW_TYPE) {
            (holder as ImageItemViewHolder).bind(context, getItem(position))
        } else {
            (holder as NetworkStateItemViewHolder).bind(networkState)
        }
    }

    private fun hasExtraRow(): Boolean {
        return networkState != null && networkState != NetworkState.LOADED
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount - 1) {
            NETWORK_VIEW_TYPE
        } else {
            IMAGE_VIEW_TYPE
        }
    }

    class ImageDiffCallback : DiffUtil.ItemCallback<Photo>() {

        override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
            return oldItem == newItem
        }

    }

    class ImageItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(context: Context, photo: Photo?) {
            val url =
                "https://live.staticflickr.com/${photo?.server}/${photo?.id}_${photo?.secret}.jpg"

            Glide.with(itemView.context)
                .load(url)
                .into(itemView.image)

            itemView.setOnClickListener {
                val intent = Intent(context, ViewActivity::class.java)
                intent.putExtra("url", url)
                context.startActivity(intent)
            }
        }
    }

    class NetworkStateItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(networkState: NetworkState?) {
            if (networkState != null && networkState == NetworkState.LOADING) {
                itemView.progress_bar_item.visibility = View.VISIBLE;
            } else {
                itemView.progress_bar_item.visibility = View.GONE;
            }

            if (networkState != null && networkState == NetworkState.ERROR) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            } else if (networkState != null && networkState == NetworkState.END_OF_LIST) {
                itemView.error_msg_item.visibility = View.VISIBLE;
                itemView.error_msg_item.text = networkState.msg;
            } else {
                itemView.error_msg_item.visibility = View.GONE;
            }
        }
    }

    fun setNetworkState(newNetworkState: NetworkState) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()

        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {                                                                      // hadExtraRow is true and hasExtraRow false
                notifyItemRemoved(super.getItemCount())                                             // remove the progressbar at the end
            } else {                                                                                // hasExtraRow is true and hadExtraRow false
                notifyItemInserted(super.getItemCount())                                            // add the progressbar at the end
            }
        } else if (hasExtraRow && previousState != newNetworkState) {                               // hasExtraRow is true and hadExtraRow true and (NetworkState.ERROR or NetworkState.END_OF_LIST)
            notifyItemChanged(itemCount - 1)                                                // add the network message at the end
        }

    }
}