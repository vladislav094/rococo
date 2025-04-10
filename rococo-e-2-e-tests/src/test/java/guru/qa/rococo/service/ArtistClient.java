package guru.qa.rococo.service;

import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.model.rest.PaintingJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface ArtistClient {

    @Nonnull
    ArtistJson createArtist(ArtistJson artistJson);

    ArtistJson getArtistByName(@Nonnull String name);

    @Nullable
    ArtistJson getArtistById(@Nonnull String id);
}
