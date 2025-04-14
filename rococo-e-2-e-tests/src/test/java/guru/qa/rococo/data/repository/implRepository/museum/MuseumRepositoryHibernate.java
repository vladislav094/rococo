package guru.qa.rococo.data.repository.implRepository.museum;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.artist.ArtistEntity;
import guru.qa.rococo.data.entity.museum.CountryEntity;
import guru.qa.rococo.data.entity.museum.GeoEntity;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import guru.qa.rococo.data.jpa.EntityManagers;
import guru.qa.rococo.data.repository.MuseumRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class MuseumRepositoryHibernate implements MuseumRepository {

    private static final Logger LOG = LoggerFactory.getLogger(MuseumRepositoryHibernate.class);

    private static final Config CFG = Config.getInstance();

    @PersistenceContext
    private final EntityManager entityManager = EntityManagers.em(CFG.museumJdbcUrl());

    @Override
    @NotNull
    @Transactional
    public MuseumEntity createMuseum(@NotNull MuseumEntity museum) {
        entityManager.joinTransaction();
        entityManager.persist(museum);
        return museum;
    }

    @Override
    @Transactional
    public Optional<MuseumEntity> findMuseumByTitle(@Nonnull String title) {
        Objects.requireNonNull(title, "Museum title cannot be null");
        try {
            return entityManager.createQuery("SELECT m FROM MuseumEntity m WHERE m.title = :title", MuseumEntity.class)
                    .setParameter("title", title.trim())
                    .getResultStream()
                    .findFirst();
        } catch (Exception e) {
            LOG.error("Error finding MuseumEntity by title: {}", title, e);
            return Optional.empty();
        }
    }

    @Override
    @NotNull
    @Transactional
    public GeoEntity createGeo(GeoEntity geo) {
        entityManager.joinTransaction();
        entityManager.persist(geo);
        return geo;
    }

    @Override
    @Transactional
    public Optional<GeoEntity> findGeoByCity(@Nonnull String city) {
        Objects.requireNonNull(city, "City name cannot be null");
        try {
            return entityManager.createQuery("SELECT g FROM GeoEntity g WHERE g.city = :city", GeoEntity.class)
                    .setParameter("city", city.trim())
                    .getResultStream()
                    .findFirst();
        } catch (Exception e) {
            LOG.error("Error finding GeoEntity by city: {}", city, e);
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public CountryEntity createCountry(CountryEntity country) {
        entityManager.joinTransaction();
        entityManager.persist(country);
        return country;
    }

    @Override
    @Transactional
    public Optional<CountryEntity> findCountryByName(@Nonnull String name) {
        Objects.requireNonNull(name, "Country name cannot be null");
        try {
            return entityManager.createQuery("SELECT с FROM CountryEntity с WHERE с.name = :name", CountryEntity.class)
                    .setParameter("name", name.trim())
                    .getResultStream()
                    .findFirst();
        } catch (Exception e) {
            LOG.error("Error finding CountryEntity by name: {}", name, e);
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<MuseumEntity> findMuseumById(@NotNull UUID id) {
        return Optional.ofNullable(entityManager.find(MuseumEntity.class, id));
    }
}
