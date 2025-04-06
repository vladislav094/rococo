package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.entity.museum.CountryEntity;
import guru.qa.rococo.data.entity.museum.GeoEntity;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface MuseumRepository {

    MuseumEntity createMuseum(MuseumEntity user);

    Optional<MuseumEntity> findMuseumByTitle(@NotNull String title);

    GeoEntity createGeo(GeoEntity geo);

    Optional<GeoEntity> findGeoByCity(@Nonnull String city);

    CountryEntity createCountry(CountryEntity country);

    Optional<CountryEntity> findCountryByName(@Nonnull String name);

}
