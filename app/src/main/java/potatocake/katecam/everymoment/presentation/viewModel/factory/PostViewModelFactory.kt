package potatocake.katecam.everymoment.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import potatocake.katecam.everymoment.data.repository.MyInfoRepository
import potatocake.katecam.everymoment.data.repository.PostRepository
import potatocake.katecam.everymoment.presentation.viewModel.PostViewModel

class PostViewModelFactory(private val postRepository: PostRepository, private val myInfoRepository: MyInfoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PostViewModel::class.java)) {
            return PostViewModel(postRepository, myInfoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}