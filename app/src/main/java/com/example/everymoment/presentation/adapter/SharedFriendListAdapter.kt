package com.example.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.everymoment.R
import com.example.everymoment.data.model.network.dto.response.Friends
import com.example.everymoment.databinding.FriendItemBinding
import com.example.everymoment.presentation.viewModel.ShareViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class SharedFriendListAdapter(private val viewModel: ShareViewModel) : ListAdapter<Friends, SharedFriendListAdapter.SharedFriendListViewHolder>(
    object : DiffUtil.ItemCallback<Friends>() {
        override fun areItemsTheSame(oldItem: Friends, newItem: Friends): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Friends, newItem: Friends): Boolean {
            return oldItem == newItem
        }
    }
) {
    private var selectedPosition = RecyclerView.NO_POSITION
    inner class SharedFriendListViewHolder(private val binding: FriendItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Friends) {
            binding.friendName.text = item.nickname

            if (item.profileImageUrl == null) {
                binding.friendImage.setImageResource(R.drawable.account_circle_24px)
            } else {
                Glide.with(itemView.context)
                    .load(item.profileImageUrl)
                    .circleCrop()
                    .into(binding.friendImage)
            }

            updateRingBackground(adapterPosition == selectedPosition)


            binding.friendContainer.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val oldPosition = selectedPosition
                    selectedPosition = if (selectedPosition == position) {
                        RecyclerView.NO_POSITION
                    } else {
                        position
                    }

                    if (oldPosition != RecyclerView.NO_POSITION) {
                        notifyItemChanged(oldPosition)
                    }
                    notifyItemChanged(position)
                    viewModel.fetchFriendDiaryList(item.id)
                    viewModel.setSelectedFriendName(item.nickname)
                }
            }
        }

        private fun updateRingBackground(isSelected: Boolean) {
            binding.storyRing.setBackgroundResource(
                if (isSelected) R.drawable.story_ring_updated
                else R.drawable.story_ring_not_updated
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SharedFriendListViewHolder {
        val binding = FriendItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SharedFriendListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SharedFriendListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}