package com.example.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.everymoment.R
import com.example.everymoment.data.model.network.dto.response.Member
import com.example.everymoment.databinding.FriendRequestItemBinding
import com.example.everymoment.extensions.CustomDialog
import com.example.everymoment.presentation.viewModel.FriendRequestViewModel

class FriendRequestAdapter(
    private val activity: FragmentActivity,
    private val viewModel: FriendRequestViewModel
) : ListAdapter<Member, FriendRequestAdapter.FriendRequestViewHolder>(
    FriendRequestDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        val binding =
            FriendRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendRequestViewHolder(binding, activity, viewModel)
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FriendRequestViewHolder(
        private val binding: FriendRequestItemBinding,
        private val activity: FragmentActivity,
        private val viewModel: FriendRequestViewModel
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: Member) {
            binding.userNickname.text = user.nickname

            if (user.friendRequestStatus == "FRIEND") {
                binding.friendButton.visibility = VISIBLE
                binding.friendRequestButton.visibility = GONE
                binding.requestCompletedButton.visibility = GONE
            } else if (user.friendRequestStatus == "SELF") {
                binding.requestCompletedButton.visibility = GONE
                binding.friendButton.visibility = GONE
                binding.friendRequestButton.visibility = GONE
            } else if (user.friendRequestStatus == "SENT") {
                binding.requestCompletedButton.visibility = VISIBLE
                binding.friendButton.visibility = GONE
                binding.friendRequestButton.visibility = GONE
            } else {
                binding.friendButton.visibility = GONE
                binding.friendRequestButton.visibility = VISIBLE
                binding.requestCompletedButton.visibility = GONE
            }

            if (user.profileImageUrl == null) {
                binding.profile.setImageResource(R.drawable.account_circle_24px)
            } else {
                binding.profile.setPadding(15, 15, 15, 15)
                Glide.with(itemView.context)
                    .load(user.profileImageUrl)
                    .circleCrop()
                    .into(binding.profile)
            }

            binding.friendRequestButton.setOnClickListener {
                showFriendRequestConfirmationDialog(user)
            }
        }


        private fun showFriendRequestConfirmationDialog(user: Member) {
            CustomDialog("${user.nickname}님에게\n친구 신청을 하시겠습니까?", "취소", "신청", onPositiveClick = {
                viewModel.sendFriendRequest(user.id)
                binding.friendRequestButton.visibility = GONE
                binding.requestCompletedButton.visibility = VISIBLE
            }).show(activity.supportFragmentManager, "CustomDialog")
        }
    }

    class FriendRequestDiffCallback : DiffUtil.ItemCallback<Member>() {
        override fun areItemsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Member, newItem: Member): Boolean {
            return oldItem == newItem
        }
    }
}