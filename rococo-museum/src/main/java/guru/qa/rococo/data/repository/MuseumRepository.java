package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.MuseumEntity;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MuseumRepository extends JpaRepository<MuseumEntity, UUID> {

    @Nonnull
    @Query("SELECT m FROM MuseumEntity m WHERE LOWER(m.title) LIKE LOWER(CONCAT(:title, '%'))" +
            " OR LOWER(m.title) LIKE LOWER(CONCAT('% ', :title, '%'))")
    Page<MuseumEntity> findByTitle(@Param("title") String title, Pageable pageable);
}
