package potatocake.katecam.everymoment.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import potatocake.katecam.everymoment.data.repository.FriendRepository
import potatocake.katecam.everymoment.data.repository.NotificationRepository
import potatocake.katecam.everymoment.presentation.viewModel.NotificationViewModel

class NotificationViewModelFactory(
    private val notificationRepository: NotificationRepository,
    private val friendRepository: FriendRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
            return NotificationViewModel(notificationRepository, friendRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}