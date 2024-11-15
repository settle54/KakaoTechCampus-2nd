package potatocake.katecam.everymoment.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import potatocake.katecam.everymoment.data.repository.impl.MyInfoRepositoryImpl
import potatocake.katecam.everymoment.presentation.viewModel.SettingViewModel

class SettingViewModelFactory(
    private val myInfoRepository: MyInfoRepositoryImpl
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(myInfoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}