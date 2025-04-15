package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.PaintingEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaintingRepository extends JpaRepository<PaintingEntity, UUID> {

    @Nonnull
    @Query("SELECT p FROM PaintingEntity p WHERE LOWER(p.title) LIKE LOWER(CONCAT(:title, '%'))" +
            " OR LOWER(p.title) LIKE LOWER(CONCAT('% ', :title, '%'))")
    Page<PaintingEntity> findByTitle(@Param("title") String title, Pageable pageable);

    Optional<PaintingEntity> findByTitle(@Param("title") String title);

    @Nonnull
    Page<PaintingEntity> findPaintingEntitiesByArtistId(UUID uuid, Pageable pageable);
}
