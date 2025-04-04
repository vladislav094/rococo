package guru.qa.rococo.data.dao.auth;

import guru.qa.rococo.data.entity.auth.AuthUserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {
    
    AuthUserEntity create(AuthUserEntity user);

    AuthUserEntity update(AuthUserEntity user);

    Optional<AuthUserEntity> findById(UUID id);

    Optional<AuthUserEntity> findByUsername(String username);

    List<AuthUserEntity> findAll();

    void remove(AuthUserEntity user);
}
