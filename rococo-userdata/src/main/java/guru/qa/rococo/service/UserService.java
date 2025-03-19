package guru.qa.rococo.service;

import guru.qa.rococo.data.UserEntity;
import guru.qa.rococo.data.projection.UserWithStatus;
import guru.qa.rococo.data.repository.UserRepository;
import guru.qa.rococo.ex.NotFoundException;
import guru.qa.rococo.ex.SameUsernameException;
import guru.qa.rococo.model.UserJson;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;


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
//
//  @Transactional(readOnly = true)
//  public @Nonnull
//  List<UserJsonBulk> allUsers(@Nonnull String username,
//                              @Nullable String searchQuery) {
//    List<UserWithStatus> usersFromDb = searchQuery == null
//        ? userRepository.findByUsernameNot(username)
//        : userRepository.findByUsernameNot(username, searchQuery);
//
//    return usersFromDb.stream()
//        .map(UserJsonBulk::fromUserEntityProjection)
//        .toList();
//  }
//
//  @Transactional(readOnly = true)
//  public @Nonnull
//  Page<UserJsonBulk> allUsers(@Nonnull String username,
//                              @Nonnull Pageable pageable,
//                              @Nullable String searchQuery) {
//    Page<UserWithStatus> usersFromDb = searchQuery == null
//        ? userRepository.findByUsernameNot(username, pageable)
//        : userRepository.findByUsernameNot(username, searchQuery, pageable);
//
//    return usersFromDb.map(UserJsonBulk::fromUserEntityProjection);
//  }
//
//  @Transactional(readOnly = true)
//  public @Nonnull
//  List<UserJsonBulk> friends(@Nonnull String username,
//                             @Nullable String searchQuery) {
//    List<UserWithStatus> usersFromDb = searchQuery == null
//        ? userRepository.findFriends(getRequiredUser(username))
//        : userRepository.findFriends(getRequiredUser(username), searchQuery);
//
//    return usersFromDb.stream()
//        .map(UserJsonBulk::fromFriendEntityProjection)
//        .toList();
//  }
//
//  @Transactional(readOnly = true)
//  public @Nonnull
//  Page<UserJsonBulk> friends(@Nonnull String username,
//                             @Nonnull Pageable pageable,
//                             @Nullable String searchQuery) {
//    Page<UserWithStatus> usersFromDb = searchQuery == null
//        ? userRepository.findFriends(getRequiredUser(username), pageable)
//        : userRepository.findFriends(getRequiredUser(username), searchQuery, pageable);
//
//    return usersFromDb.map(UserJsonBulk::fromFriendEntityProjection);
//  }
////
////  @Transactional
////  public UserJson createFriendshipRequest(@Nonnull String username, @Nonnull String targetUsername) {
////    if (Objects.equals(username, targetUsername)) {
////      throw new SameUsernameException("Can`t create friendship request for self user");
////    }
////    UserEntity currentUser = getRequiredUser(username);
////    UserEntity targetUser = getRequiredUser(targetUsername);
////    currentUser.addFriends(FriendshipStatus.PENDING, targetUser);
////    userRepository.save(currentUser);
////    return UserJson.fromEntity(targetUser, INVITE_SENT);
////  }
////
////  @Transactional
////  public @Nonnull
////  UserJson acceptFriendshipRequest(@Nonnull String username, @Nonnull String targetUsername) {
////    if (Objects.equals(username, targetUsername)) {
////      throw new SameUsernameException("Can`t accept friendship request for self user");
////    }
////    UserEntity currentUser = getRequiredUser(username);
////    UserEntity targetUser = getRequiredUser(targetUsername);
////
////    FriendshipEntity invite = currentUser.getFriendshipAddressees()
////        .stream()
////        .filter(fe -> fe.getRequester().equals(targetUser))
////        .findFirst()
////        .orElseThrow(() -> new NotFoundException("Can`t find invitation from username: '" + targetUsername + "'"));
////
////    invite.setStatus(FriendshipStatus.ACCEPTED);
////    currentUser.addFriends(FriendshipStatus.ACCEPTED, targetUser);
////    userRepository.save(currentUser);
////    return UserJson.fromEntity(targetUser, FRIEND);
////  }
//
//  @Transactional
//  public @Nonnull
//  UserJson declineFriendshipRequest(@Nonnull String username, @Nonnull String targetUsername) {
//    if (Objects.equals(username, targetUsername)) {
//      throw new SameUsernameException("Can`t decline friendship request for self user");
//    }
//    UserEntity currentUser = getRequiredUser(username);
//    UserEntity targetUser = getRequiredUser(targetUsername);
//
//    currentUser.removeInvites(targetUser);
//    targetUser.removeFriends(currentUser);
//
//    userRepository.save(currentUser);
//    userRepository.save(targetUser);
//    return UserJson.fromEntity(targetUser);
//  }
//
//  @Transactional
//  public void removeFriend(@Nonnull String username, @Nonnull String targetUsername) {
//    if (Objects.equals(username, targetUsername)) {
//      throw new SameUsernameException("Can`t remove friendship relation for self user");
//    }
//    UserEntity currentUser = getRequiredUser(username);
//    UserEntity targetUser = getRequiredUser(targetUsername);
//
//    currentUser.removeFriends(targetUser);
//    currentUser.removeInvites(targetUser);
//    targetUser.removeFriends(currentUser);
//    targetUser.removeInvites(currentUser);
//
//    userRepository.save(currentUser);
//    userRepository.save(targetUser);
//  }
//
//  public static boolean isPhotoString(String photo) {
//    return photo != null && photo.startsWith("data:image");
//  }
//
//  @Nonnull
//  UserEntity getRequiredUser(@Nonnull String username) {
//    return userRepository.findByUsername(username).orElseThrow(
//        () -> new NotFoundException("Can`t find user by username: '" + username + "'")
//    );
//  }
}

