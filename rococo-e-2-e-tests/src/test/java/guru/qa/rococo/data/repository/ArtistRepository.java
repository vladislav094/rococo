package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.entity.artist.ArtistEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface ArtistRepository {

    @NotNull
    ArtistEntity create(ArtistEntity artist);

    Optional<ArtistEntity> findById(@NotNull UUID id);

    Optional<ArtistEntity> findByName(@NotNull String title);
}
