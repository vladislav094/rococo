package guru.qa.rococo.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.grpc.GeoResponse;

import javax.annotation.Nonnull;
import java.util.UUID;

public record GeoJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("city")
        String city,
        @JsonProperty("country")
        CountryJson country) {

    public static @Nonnull GeoJson fromGrpcMessage(@Nonnull GeoResponse geo) {
        return new GeoJson(
                UUID.fromString(geo.getId()),
                geo.getCity(),
                CountryJson.fromGrpcMessage(geo.getCountry())
        );
    }
}
