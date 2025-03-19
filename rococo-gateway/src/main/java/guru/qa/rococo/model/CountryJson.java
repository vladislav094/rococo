package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.grpc.CountryResponse;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CountryJson(
        @JsonProperty("id")
        UUID id,

        @NotBlank(message = "Country can not be blank")
        @Size(min = 2, max = 20, message = "Allowed country length should be from 2 to 20 characters")
        @JsonProperty("name")
        String name) {

    public static @Nonnull CountryJson fromGrpcMessage(@Nonnull CountryResponse country) {
        return new CountryJson(
                UUID.fromString(country.getId()),
                country.getName()
        );
    }
}