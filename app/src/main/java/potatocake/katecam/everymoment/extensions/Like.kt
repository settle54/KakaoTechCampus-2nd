package potatocake.katecam.everymoment.extensions

import android.util.Log
import android.widget.ImageView
import potatocake.katecam.everymoment.R

class Like(private val likeId: ImageView) {

    data class LikeState (var isLiked: Boolean, var initialize: Boolean)
    private val likeState = LikeState(false, false)

    fun setLike(liked: Boolean) {
        if (likeState.initialize) return
        likeState.isLiked = liked
        if (liked) {
            likeId.setImageResource(R.drawable.favorite_fill_24px)
        } else {
            likeId.setImageResource(R.drawable.favorite_24px)
        }
        likeState.initialize = true
    }

    fun checkIsLike(): Boolean {
        return likeState.isLiked
    }

    fun toggleLike() {
        Log.d("like", "like clicked")
        if (likeState.isLiked) {
            likeId.setImageResource(R.drawable.favorite_24px)
        } else {
            likeId.setImageResource(R.drawable.favorite_fill_24px)
        }
        likeState.isLiked = !likeState.isLiked
    }
}