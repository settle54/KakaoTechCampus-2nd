package com.example.everymoment.presentation.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.everymoment.R
import com.example.everymoment.data.repository.FriendDiaryRepository
import com.example.everymoment.data.repository.FriendRepository
import com.example.everymoment.databinding.FragmentShareViewBinding
import com.example.everymoment.presentation.adapter.SharedFriendDiaryListAdapter
import com.example.everymoment.presentation.adapter.SharedFriendListAdapter
import com.example.everymoment.presentation.view.sub.friends.FriendsListFragment
import com.example.everymoment.presentation.viewModel.ShareViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.example.everymoment.presentation.viewModel.factory.ShareViewModelFactory

class ShareViewFragment : Fragment() {
    private lateinit var binding: FragmentShareViewBinding
    private lateinit var viewModel: ShareViewModel
    private val friendDiaryRepository = FriendDiaryRepository()
    private val friendRepository = FriendRepository()
    private val calendar = Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShareViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ShareViewModelFactory(friendDiaryRepository, friendRepository)).get(ShareViewModel::class.java)

        val friendListAdapter = SharedFriendListAdapter(viewModel)
        val friendDiaryAdapter = SharedFriendDiaryListAdapter()
        setupRecyclerView(friendListAdapter, friendDiaryAdapter)
        observeFriendList(friendListAdapter)
        observeFriendDiaryList(friendDiaryAdapter)

        val initialDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        viewModel.fetchFriendsList()
        viewModel.fetchTotalFriendDiaryList(initialDate)

        binding.friendListIcon.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, FriendsListFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private fun observeFriendDiaryList(adapter: SharedFriendDiaryListAdapter) {
        viewModel.diaries.observe(viewLifecycleOwner) { friendDiaryList ->
            if (friendDiaryList.isNullOrEmpty()) {
                if (viewModel.currentFriendId == null) {
                    binding.noTodayFriendDiaryList.visibility = View.VISIBLE
                    binding.noFriendIdDiaryList.visibility = View.GONE
                } else {
                    binding.noFriendIdDiaryList.visibility = View.VISIBLE
                    binding.noTodayFriendDiaryList.visibility = View.GONE
                }
                binding.timeLineRecyclerView.visibility = View.GONE
            } else {
                binding.timeLineRecyclerView.visibility = View.VISIBLE
                binding.noTodayFriendDiaryList.visibility = View.GONE
                binding.noFriendIdDiaryList.visibility = View.GONE
                adapter.submitList(friendDiaryList)
            }
        }
    }

    private fun observeFriendList(adapter: SharedFriendListAdapter) {
        viewModel.friends.observe(viewLifecycleOwner) { friendList ->
            adapter.submitList(friendList)
        }
    }

    private fun setupRecyclerView(adapter1: SharedFriendListAdapter, adapter2: SharedFriendDiaryListAdapter) {
        binding.friendList.adapter = adapter1
        binding.timeLineRecyclerView.adapter = adapter2

        binding.friendList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.timeLineRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        binding.timeLineRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                if (!viewModel.isLoading.value!! && totalItemCount <= (lastVisibleItemPosition + 2)) {
                    viewModel.fetchNextPage()
                }
            }
        })
    }
}