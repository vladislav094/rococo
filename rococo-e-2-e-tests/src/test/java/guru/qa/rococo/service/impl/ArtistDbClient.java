package guru.qa.rococo.service.impl;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.artist.ArtistEntity;
import guru.qa.rococo.data.repository.ArtistRepository;
import guru.qa.rococo.data.repository.implRepository.artist.ArtistRepositoryHibernate;
import guru.qa.rococo.data.tpl.XaTransactionTemplate;
import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.service.ArtistClient;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class ArtistDbClient implements ArtistClient {

    private static final Config CFG = Config.getInstance();
    private final ArtistRepository artistRepository = new ArtistRepositoryHibernate();
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(CFG.artistJdbcUrl());


    @NotNull
    @Override
    public ArtistJson createArtist(ArtistJson artistJson) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(
                () -> ArtistJson.fromEntity(
                        artistRepository.create(ArtistEntity.fromJson(artistJson))
                )
        ));
    }

    @Nullable
    public ArtistJson getArtistByName(@Nonnull String name) {
        return ArtistJson.fromEntity(artistRepository.findByName(name)
                .orElseThrow()
        );
    }
}
