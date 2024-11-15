package potatocake.katecam.everymoment.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import potatocake.katecam.everymoment.data.repository.impl.DiaryRepositoryImpl
import potatocake.katecam.everymoment.presentation.viewModel.DiaryViewModel

class DiaryViewModelFactory (
    private val diaryRepositoryImpl: DiaryRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
            return DiaryViewModel(diaryRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}