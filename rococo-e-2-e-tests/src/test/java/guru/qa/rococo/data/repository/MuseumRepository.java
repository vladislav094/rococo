package guru.qa.rococo.data.repository;

import guru.qa.rococo.data.entity.artist.ArtistEntity;
import guru.qa.rococo.data.entity.museum.CountryEntity;
import guru.qa.rococo.data.entity.museum.GeoEntity;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public interface MuseumRepository {

    @NotNull
    MuseumEntity createMuseum(@NotNull MuseumEntity museum);

    Optional<MuseumEntity> findMuseumByTitle(@NotNull String title);

    Optional<MuseumEntity> findMuseumById(@NotNull UUID id);

    @NotNull
    GeoEntity createGeo(GeoEntity geo);

    Optional<GeoEntity> findGeoByCity(@NotNull String city);

    CountryEntity createCountry(CountryEntity country);

    Optional<CountryEntity> findCountryByName(@NotNull String name);

}
