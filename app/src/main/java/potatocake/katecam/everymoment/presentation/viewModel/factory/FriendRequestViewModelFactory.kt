package potatocake.katecam.everymoment.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import potatocake.katecam.everymoment.data.repository.impl.FriendRepositoryImpl
import potatocake.katecam.everymoment.presentation.viewModel.FriendRequestViewModel

class FriendRequestViewModelFactory(
    private val friendRepositoryImpl: FriendRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendRequestViewModel::class.java)) {
            return FriendRequestViewModel(friendRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}