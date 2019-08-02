package info.novatec.boundary

import info.novatec.model.Likes
import info.novatec.repository.LikesRepository
import io.micronaut.spring.tx.annotation.Transactional
import java.util.*
import javax.inject.Singleton

@Singleton
open class LikesService(
        private val likesRepository: LikesRepository
) {

    @Transactional
    open fun likeImage(imageIdentifier: UUID): Long {
        var likes = likesRepository.findById(imageIdentifier)
        return if (likes.isPresent) {
            likesRepository.update(imageIdentifier, likes.get().likes + 1)
            likes.get().likes + 1
        } else {
            likesRepository.save(Likes(imageIdentifier, 1))
            1
        }
    }

    @Transactional(readOnly = true)
    open fun findLikes(imageIdentifier: UUID): Long {
        return likesRepository.findById(imageIdentifier).orElse(Likes(imageIdentifier, 0)).likes
    }

}
