package guru.qa.rococo.service;

import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.model.rest.GeoJson;
import guru.qa.rococo.model.rest.MuseumJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface MuseumClient {

    @Nonnull
    MuseumJson createMuseum(MuseumJson museum);

    @Nullable
    MuseumJson getMuseumByTitle(@Nonnull String title);

    @Nullable
    MuseumJson getMuseumById(@Nonnull String id);

    @Nullable
    GeoJson getGeoByCity(@Nonnull String cityName);
}
