package potatocake.katecam.everymoment.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import potatocake.katecam.everymoment.data.repository.impl.FriendDiaryRepositoryImpl
import potatocake.katecam.everymoment.data.repository.impl.FriendRepositoryImpl
import potatocake.katecam.everymoment.presentation.viewModel.ShareViewModel

class ShareViewModelFactory (
    private val friendDiaryRepositoryImpl: FriendDiaryRepositoryImpl,
    private val friendRepositoryImpl: FriendRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShareViewModel::class.java)) {
            return ShareViewModel(friendRepositoryImpl, friendDiaryRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}