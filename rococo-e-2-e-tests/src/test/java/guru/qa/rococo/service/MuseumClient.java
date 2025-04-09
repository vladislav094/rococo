package guru.qa.rococo.service;

import guru.qa.rococo.model.rest.MuseumJson;

import javax.annotation.Nonnull;

public interface MuseumClient {

    @Nonnull
    MuseumJson createMuseum(MuseumJson museum);

    MuseumJson getMuseumByTitle(@Nonnull String title);
}
