package guru.qa.rococo.test.grpc;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.grpc.ArtistResponse;
import guru.qa.rococo.grpc.CountryResponse;
import guru.qa.rococo.grpc.GeoResponse;
import guru.qa.rococo.grpc.MuseumResponse;
import guru.qa.rococo.jupiter.annotation.meta.GrpcTest;
import guru.qa.rococo.model.rest.CountryJson;
import guru.qa.rococo.model.rest.GeoJson;
import guru.qa.rococo.utils.GrpcConsoleInterceptor;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.TestClassOrder;

@GrpcTest
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class BaseGrpcTest {

    protected static final Config CFG = Config.getInstance();

    protected Channel getChannel(String grpcAddress, int port) {
        return ManagedChannelBuilder.forAddress(grpcAddress, port)
                .intercept(new GrpcConsoleInterceptor())
                .usePlaintext()
                .build();
    }

    protected void assertArtistData(ArtistResponse artistResponse,
                                    String expectedId,
                                    String expectedName,
                                    String expectedBiography,
                                    String expectedPhoto) {

        final SoftAssertions softly = new SoftAssertions();

        softly.assertThat(artistResponse.getId()).isEqualTo(expectedId);
        softly.assertThat(artistResponse.getName()).isEqualTo(expectedName);
        softly.assertThat(artistResponse.getBiography()).isEqualTo(expectedBiography);
        softly.assertThat(artistResponse.getPhoto().toStringUtf8()).isEqualTo(expectedPhoto);
        softly.assertAll();
    }

    protected void assertMuseumData(MuseumResponse museumResponse,
                                    String expectedId,
                                    String expectedTitle,
                                    String expectedDescription,
                                    String expectedPhoto,
                                    GeoJson geoJson) {

        final SoftAssertions softly = new SoftAssertions();

        softly.assertThat(museumResponse.getId()).isEqualTo(expectedId);
        softly.assertThat(museumResponse.getTitle()).isEqualTo(expectedTitle);
        softly.assertThat(museumResponse.getDescription()).isEqualTo(expectedDescription);
        softly.assertThat(museumResponse.getPhoto().toStringUtf8()).isEqualTo(expectedPhoto);
        assertGeoData(softly, museumResponse.getGeo(), geoJson);

        softly.assertAll();
    }

    protected void assertGeoData(SoftAssertions softly, GeoResponse geoResponse, GeoJson expectedGeo) {

        softly.assertThat(geoResponse.getId()).isEqualTo(expectedGeo.id().toString());
        softly.assertThat(geoResponse.getCity()).isEqualTo(expectedGeo.city());
        assertCountryData(softly, geoResponse.getCountry(), expectedGeo.country());
    }

    protected void assertCountryData(SoftAssertions softly, CountryResponse countryResponse, CountryJson expectedCountry) {

        softly.assertThat(countryResponse.getId()).isEqualTo(expectedCountry.id().toString());
        softly.assertThat(countryResponse.getName()).isEqualTo(expectedCountry.name());
    }
}
