package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.ArtistEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<ArtistEntity, UUID> {

    @Nonnull
    @Query("SELECT a FROM ArtistEntity a WHERE LOWER(a.name) LIKE LOWER(CONCAT(:name, '%'))" +
            " OR LOWER(a.name) LIKE LOWER(CONCAT('% ', :name, '%'))")
    Page<ArtistEntity> findByName(@Param("name") String name, Pageable pageable);
}
