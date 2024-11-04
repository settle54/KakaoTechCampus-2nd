package com.example.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.everymoment.data.model.entity.Emotions
import com.example.everymoment.data.model.network.dto.response.Diary
import com.example.everymoment.databinding.ShareItemBinding
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import java.util.Locale


class SharedFriendDiaryListAdapter : ListAdapter<Diary, SharedFriendDiaryListAdapter.ViewHolder>(
    object : DiffUtil.ItemCallback<Diary>() {
        override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem == newItem
        }
    }
) {
    inner class ViewHolder(private val binding: ShareItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Diary) {
            val formattedDate = item.createAt.substring(0, 10)
            val formattedTime = item.createAt.substring(11, 16)
            val formattedDateAndTime = formatDateTime("$formattedDate $formattedTime")
            binding.timeText.text = formattedDateAndTime
            binding.locationNameText.text = item.locationName
            binding.addressText.text = item.address

            if (item.thumbnailResponse == null) {
                binding.detailedDiaryContainer.isGone = true
            } else {
                binding.detailedDiaryContainer.isVisible = true
                binding.diaryImageContent.isVisible = true

                Glide.with(itemView.context)
                    .load(item.thumbnailResponse.imageUrl)
                    .into(binding.diaryImageContent)
            }

            if (item.emoji == null){
                binding.emotion.visibility = View.GONE
            } else {
                binding.emotion.visibility = View.VISIBLE
                binding.emotion.text = Emotions.fromString(item.emoji)?.getEmotionUnicode()
            }

            if (item.content == null) {
                binding.diaryTextContent.isInvisible = true
            } else {
                binding.detailedDiaryContainer.isVisible = true
                binding.diaryTextContent.isVisible = true
                binding.diaryTextContent.text = item.content
            }
        }

//        private fun formatDateTime(dateTimeString: String): String {
//            val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
//            val formatter = DateTimeFormatter.ofPattern("MM월 dd일 a h:mm")
//
//            return dateTime.format(formatter)
//        }

        private fun formatDateTime(dateTimeString: String): String {
            val dateTime = LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("a h:mm")

            return if (dateTime.toLocalDate() == currentDate) {
                dateTime.format(formatter)
            } else {
                val daysBetween = ChronoUnit.DAYS.between(dateTime.toLocalDate(), currentDate)
                when {
                    daysBetween == 1L -> "어제"
                    daysBetween > 1L -> "${daysBetween}일 전"
                    else -> "오늘"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ShareItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}