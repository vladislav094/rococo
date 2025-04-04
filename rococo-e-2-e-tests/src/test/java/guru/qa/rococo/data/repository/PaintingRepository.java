package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.entity.painting.PaintingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface PaintingRepository {

    @NotNull
    PaintingEntity create(@NotNull PaintingEntity painting);

    Optional<PaintingEntity> findById(@NotNull UUID id);

    Optional<PaintingEntity> findByTitle(@NotNull String title);
}
