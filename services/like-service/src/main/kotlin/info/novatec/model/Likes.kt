package info.novatec.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class Likes(
        @Id var image: UUID,
        val likes: Long
)