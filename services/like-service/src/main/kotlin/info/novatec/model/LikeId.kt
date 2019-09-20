package info.novatec.model

import java.util.*
import javax.persistence.Embeddable

@Embeddable
data class LikeId(
        val imageIdentifier: UUID,
        val userIdentifier: UUID
)