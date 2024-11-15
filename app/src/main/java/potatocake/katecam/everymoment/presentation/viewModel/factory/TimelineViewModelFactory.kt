package potatocake.katecam.everymoment.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import potatocake.katecam.everymoment.data.repository.impl.DiaryRepositoryImpl
import potatocake.katecam.everymoment.presentation.viewModel.TimelineViewModel

class TimelineViewModelFactory(
    private val diaryRepositoryImpl: DiaryRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimelineViewModel::class.java)) {
            return TimelineViewModel(diaryRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}