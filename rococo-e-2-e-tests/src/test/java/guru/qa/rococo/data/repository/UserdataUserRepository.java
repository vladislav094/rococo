package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.entity.userdata.UserEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface UserdataUserRepository {

    @NotNull
    UserEntity create(@NotNull UserEntity user);

    Optional<UserEntity> findById(@NotNull UUID id);

    Optional<UserEntity> findByUsername(@NotNull String username);

    @NotNull
    UserEntity update(@NotNull UserEntity user);

    void remove(@NotNull UserEntity user);
}
