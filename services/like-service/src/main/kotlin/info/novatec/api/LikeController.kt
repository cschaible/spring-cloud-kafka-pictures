package info.novatec.api

import info.novatec.boundary.LikeService
import info.novatec.repository.UserRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import java.util.*

@Secured("isAuthenticated()")
@Controller("/images")
class LikeController(
        private val likeService: LikeService,
        private val userRepository: UserRepository
) {

    @Post("/{imageIdentifier}/likes")
    fun likeImage(imageIdentifier: UUID, authentication: Authentication): HttpResponse<LikesResource> {
        val authenticatedUser = userRepository.findByIdpId(authentication.attributes["sub"] as String)
        return if (authenticatedUser != null)
            HttpResponse.ok(LikesResource(
                    imageIdentifier = imageIdentifier,
                    likes = likeService.likeImage(imageIdentifier, authenticatedUser.id),
                    hasLiked = true))
        else
            HttpResponse.unauthorized()
    }

    @Get("/{imageIdentifier}/likes")
    fun findLikes(imageIdentifier: UUID, authentication: Authentication): HttpResponse<LikesResource> {
        val authenticatedUser = userRepository.findByIdpId(authentication.attributes["sub"] as String)
        return if (authenticatedUser != null)
            HttpResponse.ok(LikesResource(
                    imageIdentifier = imageIdentifier,
                    likes = likeService.findLikes(imageIdentifier),
                    hasLiked = likeService.hasLiked(imageIdentifier, authenticatedUser.id)))
        else
            HttpResponse.unauthorized()
    }
}

data class LikesResource(
        val imageIdentifier: UUID,
        val likes: Long,
        val hasLiked: Boolean
)