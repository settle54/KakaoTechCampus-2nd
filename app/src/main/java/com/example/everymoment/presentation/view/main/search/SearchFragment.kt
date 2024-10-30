package com.example.everymoment.presentation.view.main.search

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.everymoment.data.repository.DiaryRepository
import com.example.everymoment.databinding.FragmentSearchBinding
import com.example.everymoment.presentation.adapter.FriendsListAdapter
import com.example.everymoment.presentation.adapter.SearchAdapter
import com.example.everymoment.presentation.viewModel.SearchViewModel
import com.example.everymoment.presentation.viewModel.factory.SearchViewModelFactory
import com.google.android.material.internal.ViewUtils.hideKeyboard

class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var filterBottomSheet: SearchFilterDialogFragment
    private lateinit var adapter: SearchAdapter
    private val searchViewModel: SearchViewModel by activityViewModels {
        SearchViewModelFactory(DiaryRepository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchView()
        observeViewModel()
        setFilterSheet()

        searchViewModel.filterState.observe(viewLifecycleOwner) { filterState ->
            performSearch(binding.searchWindow.text.toString())
        }

        binding.filter.setOnClickListener {
            showFilterSheet()
        }
        binding.filterDesc.setOnClickListener {
            showFilterSheet()
        }
    }

    private fun setupRecyclerView() {
        adapter = SearchAdapter(requireActivity(), searchViewModel)
        binding.searchedDiaryRecyclerView.apply {
            this.adapter = this@SearchFragment.adapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setupSearchView() {
        binding.searchWindow.apply {
            imeOptions = EditorInfo.IME_ACTION_SEARCH
            inputType = InputType.TYPE_CLASS_TEXT

            setOnEditorActionListener { textView, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(textView.text.toString())
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }

        }
    }

    private fun performSearch(query: String) {
        val currentFilter = searchViewModel.filterState.value

        val keyword = if (query.isBlank()) null else query

        Log.d("SearchFilter", "검색어: $query")
        Log.d("SearchFilter", "이모지 필터: ${currentFilter?.selectedEmotions}")
        Log.d("SearchFilter", "카테고리 필터: ${currentFilter?.selectedCategories}")
        Log.d("SearchFilter", "시작 날짜: ${currentFilter?.startDate}")
        Log.d("SearchFilter", "종료 날짜: ${currentFilter?.endDate}")
        Log.d("SearchFilter", "북마크 여부: ${currentFilter?.isBookmarked}")

        searchViewModel.fetchSearchedDiaries(
            keyword = keyword,
            emoji = currentFilter?.selectedEmotions,
            category = currentFilter?.selectedCategories,
            from = currentFilter?.startDate,
            until = currentFilter?.endDate,
            bookmark = currentFilter?.isBookmarked
        )
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchWindow.windowToken, 0)
    }

    private fun observeViewModel() {
        searchViewModel.searchDiaries.observe(viewLifecycleOwner) { diaries ->
            adapter.submitList(diaries)
            binding.searchedDiaryRecyclerView.visibility =
                if (diaries.isNotEmpty()) VISIBLE else View.GONE
        }
    }

    private fun setFilterSheet() {
        filterBottomSheet = SearchFilterDialogFragment()
    }

    private fun showFilterSheet() {
        filterBottomSheet.show(parentFragmentManager, SearchFilterDialogFragment.TAG)
    }

}