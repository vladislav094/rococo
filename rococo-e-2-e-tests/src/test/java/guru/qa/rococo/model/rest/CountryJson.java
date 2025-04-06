package guru.qa.rococo.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.grpc.CountryResponse;

import javax.annotation.Nonnull;
import java.util.UUID;

public record CountryJson(@JsonProperty("id")
                          UUID id,
                          @JsonProperty("name")
                          String name) {

    public static @Nonnull CountryJson fromGrpcMessage(@Nonnull CountryResponse country) {
        return new CountryJson(
                UUID.fromString(country.getId()),
                country.getName()
        );
    }
}
