package guru.qa.rococo.data.repository.implRepository.artist;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.artist.ArtistEntity;
import guru.qa.rococo.data.jpa.EntityManagers;
import guru.qa.rococo.data.repository.ArtistRepository;
import jakarta.persistence.EntityManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class ArtistRepositoryHibernate implements ArtistRepository {

    private static final Logger LOG = LoggerFactory.getLogger(ArtistRepositoryHibernate.class);

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = EntityManagers.em(CFG.artistJdbcUrl());

    @NotNull
    @Override
    @Transactional
    public ArtistEntity create(ArtistEntity artist) {
        entityManager.joinTransaction();
        entityManager.persist(artist);
        return artist;
    }

    @Override
    @Transactional
    public Optional<ArtistEntity> findById(@NotNull UUID id) {
        return Optional.ofNullable(entityManager.find(ArtistEntity.class, id));
    }

    @Override
    @Transactional
    public Optional<ArtistEntity> findByName(@NotNull String name) {
        Objects.requireNonNull(name, "Artist name cannot be null");
        try {
            return entityManager.createQuery("SELECT a FROM ArtistEntity a WHERE a.name = :name", ArtistEntity.class)
                    .setParameter("name", name.trim())
                    .getResultStream()
                    .findFirst();
        } catch (Exception e) {
            LOG.error("Error finding ArtistEntity by name: {}", name, e);
            return Optional.empty();
        }
    }
}
