package potatocake.katecam.everymoment.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import potatocake.katecam.everymoment.data.repository.impl.FriendRepositoryImpl
import potatocake.katecam.everymoment.presentation.viewModel.FriendsListViewModel

class FriendsListViewModelFactory(
    private val friendRepositoryImpl: FriendRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendsListViewModel::class.java)) {
            return FriendsListViewModel(friendRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}