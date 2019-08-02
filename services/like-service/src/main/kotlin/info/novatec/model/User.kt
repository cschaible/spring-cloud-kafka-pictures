package info.novatec.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class User(
        @Id var id: UUID,
        val idpId: String,
        val name: String? = null
)