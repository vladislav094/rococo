package guru.qa.rococo.service.impl;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.data.entity.museum.CountryEntity;
import guru.qa.rococo.data.entity.museum.GeoEntity;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import guru.qa.rococo.data.repository.MuseumRepository;
import guru.qa.rococo.data.repository.implRepository.museum.MuseumRepositoryHibernate;
import guru.qa.rococo.data.tpl.XaTransactionTemplate;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.service.MuseumClient;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;

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
}
