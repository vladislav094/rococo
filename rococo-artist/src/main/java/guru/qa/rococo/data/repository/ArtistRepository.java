package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {
}
