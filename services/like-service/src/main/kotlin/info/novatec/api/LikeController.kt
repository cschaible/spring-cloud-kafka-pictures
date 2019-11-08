package info.novatec.api

import info.novatec.boundary.LikeService
import info.novatec.repository.UserRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.context.ServerRequestContext
import io.micronaut.http.hateoas.AbstractResource
import io.micronaut.http.hateoas.Link
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import java.net.URI
import java.util.*
import javax.annotation.security.PermitAll

@Secured(SecurityRule.IS_AUTHENTICATED)
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
                    hasLiked = true,
                    authenticated = true))
        else
            HttpResponse.unauthorized()
    }

    @PermitAll
    @Get("/{imageIdentifier}/likes")
    fun findLikes(imageIdentifier: UUID, authentication: Authentication?): HttpResponse<LikesResource> {
        val authenticatedUser = if (authentication == null) null
        else userRepository.findByIdpId(authentication.attributes["sub"] as String)
        return if (authenticatedUser != null)
            HttpResponse.ok(LikesResource(
                    imageIdentifier = imageIdentifier,
                    likes = likeService.findLikes(imageIdentifier),
                    hasLiked = likeService.hasLiked(imageIdentifier, authenticatedUser.id),
                    authenticated = true))
        else
            HttpResponse.ok(LikesResource(
                    imageIdentifier = imageIdentifier,
                    likes = likeService.findLikes(imageIdentifier),
                    hasLiked = false,
                    authenticated = false))
    }

    @Delete("/{imageIdentifier}/likes")
    fun dislikeImage(imageIdentifier: UUID, authentication: Authentication): HttpResponse<LikesResource> {
        val authenticatedUser = userRepository.findByIdpId(authentication.attributes["sub"] as String)
        return if (authenticatedUser != null)
            HttpResponse.ok(LikesResource(
                    imageIdentifier = imageIdentifier,
                    likes = likeService.dislikeImage(imageIdentifier, authenticatedUser.id),
                    hasLiked = false,
                    authenticated = true))
        else
            HttpResponse.unauthorized()
    }
}

data class LikesResource(
        val imageIdentifier: UUID,
        val likes: Long
) : AbstractResource<LikesResource>() {
    constructor(imageIdentifier: UUID,
                likes: Long,
                hasLiked: Boolean,
                authenticated: Boolean) : this(imageIdentifier, likes) {
        val currentRequest = ServerRequestContext.currentRequest<Any>().get()
        link(Link.SELF, getLink(currentRequest.uri))
        if (authenticated) {
            if (hasLiked) {
                link("dislike", getLink(currentRequest.uri))
            } else {
                link("like", getLink(currentRequest.uri))
            }

        }
    }

    private fun getLink(uri: URI): Link {
        val currentRequest = ServerRequestContext.currentRequest<Any>().get()
        val host = currentRequest.headers.getFirst("X-Forwarded-Host").get()
        val proto = currentRequest.headers.getFirst("X-Forwarded-Proto").get()
        return Link.of("$proto://$host$uri")
    }
}