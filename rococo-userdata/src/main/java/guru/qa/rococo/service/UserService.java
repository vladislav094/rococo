package guru.qa.rococo.service;

import guru.qa.rococo.data.UserEntity;
import guru.qa.rococo.data.repository.UserRepository;
import guru.qa.rococo.model.UserJson;
import jakarta.annotation.Nonnull;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @KafkaListener(topics = "users", groupId = "userdata")
    public void listener(@Payload UserJson user, ConsumerRecord<String, UserJson> cr) {
        userRepository.findByUsername(user.username())
                .ifPresentOrElse(
                        u -> LOG.info("### User already exist in DB, kafka event will be skipped: {}", cr.toString()),
                        () -> {
                            LOG.info("### Kafka consumer record: {}", cr.toString());

                            UserEntity userDataEntity = new UserEntity();
                            userDataEntity.setUsername(user.username());
                            UserEntity userEntity = userRepository.save(userDataEntity);

                            LOG.info(
                                    "### User '{}' successfully saved to database with id: {}",
                                    user.username(),
                                    userEntity.getId()
                            );
                        }
                );
    }

    @Transactional(readOnly = true)
    public @Nonnull
    UserJson getCurrentUser(@Nonnull String username) {
        return userRepository.findByUsername(username).map(UserJson::fromEntity)
                .orElseGet(() -> new UserJson(
                        null,
                        username,
                        null,
                        null,
                        null
                ));
    }
}

