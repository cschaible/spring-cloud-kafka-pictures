package info.novatec.api

import info.novatec.model.User
import info.novatec.repository.UserRepository
import info.novatec.spring.showcase.user.message.UserCreatedEventAvro
import info.novatec.spring.showcase.user.message.UserUpdatedEventAvro
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset
import io.micronaut.configuration.kafka.annotation.OffsetStrategy
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.messaging.Acknowledgement
import org.apache.avro.specific.SpecificRecordBase
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import java.util.*

@KafkaListener(groupId = "local.likes", offsetStrategy = OffsetStrategy.DISABLED, offsetReset = OffsetReset.EARLIEST)
class UserListener(
        private val userRepository: UserRepository
) {

    private val logger = LoggerFactory.getLogger(UserListener::class.java)

    @Topic("local.user")
    fun listenToUserEvent(record: ConsumerRecord<String, SpecificRecordBase>, acknowledgement: Acknowledgement) {
        var value = record.value()
        when (value) {
            is UserCreatedEventAvro -> {
                val user = User(id = UUID.fromString(value.getIdentifier()), idpId = value.getIdpId())
                userRepository.save(user)
                logger.info("User with id ${value.getIdentifier()} saved")
            }
            is UserUpdatedEventAvro -> {
                userRepository.update(id = UUID.fromString(value.getIdentifier()), name = value.getDisplayName())
                logger.info("User with id ${value.getIdentifier()} updated")
            }
        }
        acknowledgement.ack()
    }
}