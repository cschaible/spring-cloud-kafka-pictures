package info.novatec.spring.showcase.search.api;

import info.novatec.spring.showcase.image.message.ImageDeletedEventAvro;
import info.novatec.spring.showcase.image.message.ImageExifDataExtractedEventAvro;
import info.novatec.spring.showcase.image.message.ImageInvalidEventAvro;
import info.novatec.spring.showcase.image.message.ImageUploadedEventAvro;
import info.novatec.spring.showcase.search.api.assembler.ImageAssembler;
import info.novatec.spring.showcase.search.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Controller;

import java.util.UUID;

@Slf4j
@Controller
public class ImageMessagingController {

  @Value("${spring.kafka.clientId}")
  private String kafkaClientPrefix;

  @Autowired private ImageAssembler imageAssembler;

  @Autowired private ImageService imageService;

  @KafkaListener(
      topics = "#{topicConfiguration.getTopicForChannel('image')}",
      clientIdPrefix = "${spring.kafka.properties.clientId.app}.image")
  public void listenToUserEvents(
      Acknowledgment ack, ConsumerRecord<String, SpecificRecordBase> event) {
    SpecificRecordBase value = event.value();
    if (value instanceof ImageUploadedEventAvro) {
      imageService.create(imageAssembler.assemble((ImageUploadedEventAvro) value));
    } else if (value instanceof ImageExifDataExtractedEventAvro) {
      imageService.updateExifData(imageAssembler.assemble((ImageExifDataExtractedEventAvro) value));
    } else if (value instanceof ImageInvalidEventAvro) {
      imageService.deleteImage(UUID.fromString(((ImageInvalidEventAvro) value).getIdentifier()));
    } else if (value instanceof ImageDeletedEventAvro) {
      imageService.deleteImage(UUID.fromString(((ImageDeletedEventAvro) value).getIdentifier()));
    } else {
      log.debug("Skip message of type: " + event.getClass());
    }
    ack.acknowledge();
  }
}
