package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.grpc.GeoResponse;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record GeoJson(
        @JsonProperty("id")
        UUID id,

        @NotBlank(message = "City can not be blank")
        @Size(min = 2, max = 15, message = "Allowed city length should be from 2 to 15 characters")
        @JsonProperty("city")
        String city,

        @JsonProperty("country")
        CountryJson geo) {


    public static @Nonnull GeoJson fromGrpcMessage(@Nonnull GeoResponse geo) {
        return new GeoJson(
                UUID.fromString(geo.getId()),
                geo.getCity(),
                CountryJson.fromGrpcMessage(geo.getCountry())
        );
    }
}
