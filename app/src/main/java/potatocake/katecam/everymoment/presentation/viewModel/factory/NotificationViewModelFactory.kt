package potatocake.katecam.everymoment.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import potatocake.katecam.everymoment.data.repository.impl.FriendRepositoryImpl
import potatocake.katecam.everymoment.data.repository.impl.NotificationRepositoryImpl
import potatocake.katecam.everymoment.presentation.viewModel.NotificationViewModel

class NotificationViewModelFactory(
    private val notificationRepositoryImpl: NotificationRepositoryImpl,
    private val friendRepositoryImpl: FriendRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(notificationRepositoryImpl, friendRepositoryImpl) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}