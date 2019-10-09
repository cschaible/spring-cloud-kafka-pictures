package info.novatec.boundary

import info.novatec.model.Like
import info.novatec.model.LikeId
import info.novatec.repository.LikeRepository
import java.time.Instant
import java.util.*
import javax.inject.Singleton
import javax.transaction.Transactional

@Singleton
open class LikeService(
        private val likeRepository: LikeRepository
) {

    @Transactional
    open fun likeImage(imageIdentifier: UUID, userIdentifier: UUID): Long {
        val like = likeRepository.findById(LikeId(imageIdentifier, userIdentifier))
        if (!like.isPresent) {
            likeRepository.save(Like(LikeId(imageIdentifier, userIdentifier), Instant.now()))
        }
        return likeRepository.countByLikeIdImageIdentifier(imageIdentifier)
    }

    @Transactional
    open fun dislikeImage(imageIdentifier: UUID, userIdentifier: UUID): Long {
        likeRepository.deleteById(LikeId(imageIdentifier, userIdentifier))
        return likeRepository.countByLikeIdImageIdentifier(imageIdentifier)
    }

    @Transactional
    open fun findLikes(imageIdentifier: UUID): Long {
        return likeRepository.countByLikeIdImageIdentifier(imageIdentifier)
    }

    @Transactional
    open fun hasLiked(imageIdentifier: UUID, userIdentifier: UUID): Boolean {
        return likeRepository.findById(LikeId(imageIdentifier, userIdentifier)).isPresent
    }

}
