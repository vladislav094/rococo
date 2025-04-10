package guru.qa.rococo.service.impl;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.artist.ArtistEntity;
import guru.qa.rococo.data.entity.museum.CountryEntity;
import guru.qa.rococo.data.entity.museum.GeoEntity;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import guru.qa.rococo.data.repository.MuseumRepository;
import guru.qa.rococo.data.repository.implRepository.museum.MuseumRepositoryHibernate;
import guru.qa.rococo.data.tpl.XaTransactionTemplate;
import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.model.rest.GeoJson;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.service.MuseumClient;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.NotFoundException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class MuseumDbClient implements MuseumClient {

    private static final Config CFG = Config.getInstance();
    private final MuseumRepository museumRepository = new MuseumRepositoryHibernate();
    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(CFG.museumJdbcUrl());

    @NotNull
    @Override
    public MuseumJson createMuseum(MuseumJson museumJson) {
        return Objects.requireNonNull(
                xaTransactionTemplate.execute(() -> {
                            MuseumEntity museumEntity = MuseumEntity.fromJson(museumJson);
                            CountryEntity countryEntity = CountryEntity.fromJson(museumJson.geo().country());

                            if (museumEntity.getGeo().getId() == null) {
                                Optional<GeoEntity> existingGeoEntity = museumRepository.findGeoByCity(museumEntity.getGeo().getCity());
                                if (existingGeoEntity.isPresent()) {
                                    museumEntity.setGeo(existingGeoEntity.get());
                                } else {
                                    CountryEntity savedCountry = museumRepository.findCountryByName(museumJson.geo().country().name())
                                            .orElseGet(
                                                    () -> museumRepository.createCountry(countryEntity)
                                            );
                                    GeoEntity geoEntity = new GeoEntity();
                                    geoEntity.setCity(museumJson.geo().city());
                                    geoEntity.setCountry(savedCountry);
                                    GeoEntity savedGeo = museumRepository.createGeo(geoEntity);

                                    museumEntity.setGeo(savedGeo);
                                }
                            }
                            return MuseumJson.fromEntity(museumRepository.createMuseum(museumEntity));
                        }
                )
        );
    }

    @Nullable
    public MuseumJson getMuseumByTitle(@Nonnull String title) {
        return MuseumJson.fromEntity(museumRepository.findMuseumByTitle(title)
                .orElseThrow(() -> new NotFoundException("Museum with title: '" + title + "' not found"))
        );
    }

    @Nullable
    public GeoJson getGeoByCity(@Nonnull String cityName) {
        return GeoJson.fromEntity(museumRepository.findGeoByCity(cityName)
                .orElseThrow(() -> new NotFoundException("Geo with city: '" + cityName + "' not found")));
    }

    @Nullable
    public MuseumJson getMuseumById(@Nonnull String id) {
        Optional<MuseumEntity> entity = museumRepository.findMuseumById(UUID.fromString(id));
        return entity.map(MuseumJson::fromEntity)
                .orElseThrow(() -> new NotFoundException("Museum with id: '" + id + "' not found"));
    }
}
