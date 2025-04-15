package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.UserEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    @Nonnull
    Optional<UserEntity> findByUsername(@Nonnull String username);
}
