package potatocake.katecam.everymoment.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import potatocake.katecam.everymoment.data.repository.impl.DiaryRepositoryImpl
import potatocake.katecam.everymoment.presentation.viewModel.SearchViewModel

class SearchViewModelFactory(
    private val diaryRepositoryImpl: DiaryRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(diaryRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}