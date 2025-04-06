package guru.qa.rococo.data.repository.implRepository.museum;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.museum.CountryEntity;
import guru.qa.rococo.data.entity.museum.GeoEntity;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import guru.qa.rococo.data.jpa.EntityManagers;
import guru.qa.rococo.data.repository.MuseumRepository;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Optional;

public class MuseumRepositoryHibernate implements MuseumRepository {

    private static final Logger LOG = LoggerFactory.getLogger(MuseumRepositoryHibernate.class);

    private static final Config CFG = Config.getInstance();
    private final EntityManager entityManager = EntityManagers.em(CFG.museumJdbcUrl());

    @Override
    public MuseumEntity createMuseum(MuseumEntity museum) {
        entityManager.joinTransaction();
        entityManager.persist(museum);
        return museum;
    }

    @Override
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
    public GeoEntity createGeo(GeoEntity geo) {
        entityManager.joinTransaction();
        entityManager.persist(geo);
        return geo;
    }

    @Override
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
    public CountryEntity createCountry(CountryEntity country) {
        entityManager.joinTransaction();
        entityManager.persist(country);
        return country;
    }

    @Override
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
}
