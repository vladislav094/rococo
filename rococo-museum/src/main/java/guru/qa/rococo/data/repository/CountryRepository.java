package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import jakarta.annotation.Nonnull;

import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {

    Optional<CountryEntity> findByName(@Nonnull String name);
}
