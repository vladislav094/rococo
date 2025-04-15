package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.entity.auth.AuthUserEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {

    @NotNull
    AuthUserEntity create(AuthUserEntity user);

    @NotNull
    AuthUserEntity update(AuthUserEntity user);

    Optional<AuthUserEntity> findById(@NotNull UUID id);

    Optional<AuthUserEntity> findByUsername(@NotNull String username);

    void remove(@NotNull AuthUserEntity user);

}
