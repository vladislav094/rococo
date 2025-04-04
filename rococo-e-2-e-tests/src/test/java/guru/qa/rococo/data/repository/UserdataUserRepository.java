package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.entity.userdata.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserdataUserRepository {

    UserEntity create(UserEntity user);

    Optional<UserEntity> findById(UUID id);

    Optional<UserEntity> findByUsername(String username);

    UserEntity update(UserEntity user);

    void remove(UserEntity user);
}
