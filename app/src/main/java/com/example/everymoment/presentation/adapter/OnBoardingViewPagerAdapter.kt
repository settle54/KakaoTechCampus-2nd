package com.example.everymoment.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.R
import com.example.everymoment.data.model.network.dto.vo.AppGuide
import com.example.everymoment.databinding.ItemOnboardingBinding

class OnBoardingViewPagerAdapter :
    RecyclerView.Adapter<OnBoardingViewPagerAdapter.PagerViewHolder>() {

    private val pages = listOf(
        AppGuide(
            "자동 기록",
            "한 장소에서 사용자가 설정한 시간이 지나면\n일기가 자동기록됩니다",
            R.drawable.dummy_1
        ),
        AppGuide(
            "앱 설정",
            "정확한 자동기록을 위해 절전모드는 항상 OFF\n 위치는 항상 허용해주세요",
            R.drawable.dummy_4
        ),
        AppGuide(
            "피드 기능",
            "친구와 일기를 공유해보세요",
            R.drawable.dummy_2
        ),
        AppGuide(
            "캘린더",
            "캘린더에서 날짜를 누르면 그 날짜에 쓴\n 일기 화면으로 이동합니다",
            R.drawable.dummy_5
        ),
        AppGuide(
            "일기 검색",
            "자신의 일기를 다양한 필터로 검색해보세요",
            R.drawable.dummy_3
        ),
        AppGuide(
            "설정",
            "프로필,닉네임,알림 설정 등\n 자신에게 맞는 설정을 해보세요",
            R.drawable.dummy_6
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagerViewHolder {
        val binding = ItemOnboardingBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    override fun getItemCount() = pages.size

    class PagerViewHolder(private val binding: ItemOnboardingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(page: AppGuide) {
            binding.appGuideTitle.text = page.title
            binding.appGuideDescription.text = page.description
            binding.appGuideImage.setImageResource(page.imageResId)
        }
    }
}