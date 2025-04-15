package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.GeoEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GeoRepository extends JpaRepository<GeoEntity, UUID> {

    Optional<GeoEntity> findByCity(@Nonnull String city);
}
