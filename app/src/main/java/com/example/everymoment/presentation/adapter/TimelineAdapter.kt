package com.example.everymoment.presentation.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.everymoment.R
import com.example.everymoment.data.model.entity.Emotions
import com.example.everymoment.data.model.network.dto.response.Diary
import com.example.everymoment.databinding.TimelineItemBinding
import com.example.everymoment.extensions.CustomDialog
import com.example.everymoment.extensions.EmotionPopup
import com.example.everymoment.extensions.ToPxConverter
import com.example.everymoment.presentation.view.sub.diary.DiaryReadFragment
import com.example.everymoment.presentation.viewModel.TimelineViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimelineAdapter(private val activity: FragmentActivity, private val viewModel: TimelineViewModel) : ListAdapter<Diary, TimelineAdapter.TimelineViewHolder>(
    object : DiffUtil.ItemCallback<Diary>() {
        override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class TimelineViewHolder(private val binding: TimelineItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Diary) {
            binding.root.setOnClickListener {
                val diaryReadFragment = DiaryReadFragment()
                val bundle = Bundle().apply {
                    putInt("diary_id", item.id)
                }
                diaryReadFragment.arguments = bundle

                val fragmentManager = (binding.root.context as AppCompatActivity).supportFragmentManager
                fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, diaryReadFragment)
                    .addToBackStack(null)
                    .commit()
            }

            binding.timeText.text = item.createAt.substring(11, 16)
            binding.locationNameText.text = item.locationName
            binding.addressText.text = item.address

            if (item.emoji == null){
                binding.addEmotion.visibility = View.VISIBLE
                binding.emotion.visibility = View.GONE
            } else {
                binding.addEmotion.visibility = View.GONE
                binding.emotion.visibility = View.VISIBLE
                binding.emotion.text = Emotions.fromString(item.emoji)?.getEmotionUnicode()
            }

            val emotionPopupManager = EmotionPopup(binding.root.context) { selectedEmotion ->
                val emotionName = selectedEmotion.toString().lowercase()
                viewModel.viewModelScope.launch {
                    try {
                        viewModel.updateEmotions(item.id, emotionName)
                        withContext(Dispatchers.Main) {
                            binding.addEmotion.visibility = View.GONE
                            binding.emotion.visibility = View.VISIBLE
                            binding.emotion.text = selectedEmotion.getEmotionUnicode()
                        }
                    } catch (e: Exception) {
                        Log.e("arieum", "Error updating emotion", e)
                    }
                }
            }

            binding.addEmotion.setOnClickListener {
                emotionPopupManager.showEmotionsPopup(it, ToPxConverter().dpToPx(10))
            }

            binding.emotion.setOnClickListener {
                emotionPopupManager.showEmotionsPopup(it, ToPxConverter().dpToPx(10))
            }

            var isBookmarked = item.bookmark
            updateBookmarkIcon(isBookmarked)

            binding.bookmarkIcon.setOnClickListener {
                isBookmarked = !isBookmarked
                updateBookmarkIcon(isBookmarked)
                viewModel.updateBookmarkStatus(item.id)
                binding.root.context.showToast(
                    if (isBookmarked) R.string.add_bookmark else R.string.remove_bookmark
                )
            }

            var isShared = item.public
            updateShareIcon(isShared)

            binding.shareIcon.setOnClickListener {
                isShared = !isShared
                updateShareIcon(isShared)
                viewModel.updateShareStatus(item.id)
                binding.root.context.showToast(
                    if (isShared) R.string.is_public else R.string.is_private
                )
            }

            if (item.thumbnailResponse == null) {
                binding.detailedDiaryContainer.isGone = true
            } else {
                binding.detailedDiaryContainer.isVisible = true
                binding.diaryImageContent.isVisible = true

                Glide.with(itemView.context)
                    .load(item.thumbnailResponse.imageUrl)
                    .into(binding.diaryImageContent)
            }

            if (item.content == null) {
                binding.diaryTextContent.isGone = true
            } else {
                binding.detailedDiaryContainer.isVisible = true
                binding.diaryTextContent.isVisible = true

                binding.diaryTextContent.text = item.content
            }

            binding.deleteIcon.setOnClickListener {
                CustomDialog("이 일기를 삭제하시겠습니까?", "취소", "삭제", onPositiveClick = {
                    removeItem(adapterPosition)
                    viewModel.deleteDiary(item.id)
                }).show(activity.supportFragmentManager, "delAutoDiary")
            }

            binding.editIcon.setOnClickListener {
                viewModel.getPlaceNamesForDiary(item.id) { placeNames ->
                    (binding.root.context as? FragmentActivity)?.runOnUiThread {
                        if (placeNames.isNotEmpty()) {
                            val popupMenu = PopupMenu(binding.root.context, binding.editIcon, Gravity.NO_GRAVITY, 0, R.style.CustomPopupMenu)
                            placeNames.forEach { placeName ->
                                popupMenu.menu.add(placeName)
                            }
                            popupMenu.setOnMenuItemClickListener { menuItem ->
                                binding.locationNameText.text = menuItem.title
                                viewModel.updateDiaryLocation(item.id, menuItem.title.toString())
                                true
                            }

                            try {
                                popupMenu.show()
                            } catch (e: Exception) {
                                Log.e("arieum", "Error showing popup menu", e)
                            }
                        } else {
                            Toast.makeText(
                                binding.root.context,
                                "해당위치에 대한 장소명을 찾을 수 없습니다",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        private fun updateBookmarkIcon(isBookmarked: Boolean) {
            binding.bookmarkIcon.setImageResource(
                if (isBookmarked) R.drawable.baseline_bookmark_24 else R.drawable.bookmark_26dp_5f6368_fill0_wght300_grad0_opsz24
            )
        }

        private fun updateShareIcon(isShared: Boolean) {
            binding.shareIcon.setImageResource(
                if (isShared) R.drawable.ic_is_shared else R.drawable.share_26dp_5f6368_fill0_wght300_grad0_opsz24
            )
        }

        private fun removeItem(position: Int) {
            val newList = currentList.toMutableList()
            newList.removeAt(position)
            submitList(newList)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimelineViewHolder {
        val binding = TimelineItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TimelineViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TimelineViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun Context.showToast(messageResId: Int) {
        Toast.makeText(this, getString(messageResId), Toast.LENGTH_SHORT).show()
    }
}