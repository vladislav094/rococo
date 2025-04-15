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
import guru.qa.rococo.model.rest.CountryJson;
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
    private final MuseumRepository museumRepository;
    private final XaTransactionTemplate xaTransactionTemplate;

    public MuseumDbClient() {
        this.museumRepository = new MuseumRepositoryHibernate();
        this.xaTransactionTemplate = new XaTransactionTemplate(CFG.museumJdbcUrl());
    }

    @NotNull
    @Override
    public MuseumJson createMuseum(MuseumJson museumJson) {
        return Objects.requireNonNull(
                xaTransactionTemplate.execute(() -> {
                            MuseumEntity museumEntity = MuseumEntity.fromJson(museumJson);
                            GeoEntity geoEntity = resolveOrCreateGeo(museumJson.geo());
                            museumEntity.setGeo(geoEntity);
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

    @NotNull
    public GeoJson createGeo(GeoJson geoJson) {
        return Objects.requireNonNull(xaTransactionTemplate.execute(() ->
                GeoJson.fromEntity(resolveOrCreateGeo(geoJson))
        ));
    }

    private GeoEntity resolveOrCreateGeo(GeoJson geoJson) {
        return museumRepository.findGeoByCity(geoJson.city())
                .orElseGet(() -> createGeoEntity(geoJson));
    }

    private GeoEntity createGeoEntity(GeoJson geoJson) {
        CountryEntity country = resolveOrCreateCountry(geoJson.country());

        GeoEntity geo = new GeoEntity();
        geo.setCity(geoJson.city());
        geo.setCountry(country);

        return museumRepository.createGeo(geo);
    }

    private CountryEntity resolveOrCreateCountry(CountryJson countryJson) {
        return museumRepository.findCountryByName(countryJson.name())
                .orElseGet(() -> createCountryEntity(countryJson));
    }

    private CountryEntity createCountryEntity(CountryJson countryJson) {
        return museumRepository.createCountry(CountryEntity.fromJson(countryJson));
    }
}
