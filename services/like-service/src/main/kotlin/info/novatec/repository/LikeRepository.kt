package info.novatec.repository

import info.novatec.model.Like
import info.novatec.model.LikeId
import io.micronaut.data.annotation.Query
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import java.util.*

@JdbcRepository(dialect = Dialect.MYSQL)
interface LikeRepository : CrudRepository<Like, LikeId> {

    @Query(value = "select * from likes where like_id_image_identifier like :imageIdentifier")
    fun countByImageIdentifier(imageIdentifier: UUID): List<Like>

}