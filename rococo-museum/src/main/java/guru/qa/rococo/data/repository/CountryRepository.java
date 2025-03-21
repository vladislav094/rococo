package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.CountryEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {

    Optional<CountryEntity> findByName(@Nonnull String name);
}
