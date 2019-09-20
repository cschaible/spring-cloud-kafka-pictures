package info.novatec.model

import java.time.Instant
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "likes")
data class Like(
        @EmbeddedId val likeId: LikeId,
        val insertDate: Instant
)