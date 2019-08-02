package info.novatec.api

import info.novatec.boundary.LikesService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import java.util.*

@Secured("isAuthenticated()")
@Controller("/images")
class LikeController(
        private val likesService: LikesService
) {

    @Post("/{imageIdentifier}/likes")
    fun likeImage(imageIdentifier: UUID): HttpResponse<LikesResource> {
        return HttpResponse.ok(LikesResource(image = imageIdentifier, likes = likesService.likeImage(imageIdentifier)))
    }

    @Get("/{imageIdentifier}/likes")
    fun findLikes(imageIdentifier: UUID): HttpResponse<LikesResource> {
        return HttpResponse.ok(LikesResource(image = imageIdentifier, likes = likesService.findLikes(imageIdentifier)))
    }
}

data class LikesResource(
        val image: UUID,
        val likes: Long
)