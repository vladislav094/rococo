package guru.qa.rococo.service;

import guru.qa.rococo.model.rest.PaintingJson;

import javax.annotation.Nonnull;

public interface PaintingClient {

    @Nonnull
    PaintingJson createPainting(PaintingJson paintingJson);

    PaintingJson getPaintingByTitle(@Nonnull String title);
}
