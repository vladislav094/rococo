package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.rococo.config.RococoGatewayServiceConfig;
import guru.qa.rococo.grpc.MuseumResponse;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record MuseumJson(
        @JsonProperty("id")
        UUID id,

        @NotBlank(message = "Museum can not be blank")
        @Size(min = 2, max = 50, message = "Allowed museum length should be from 2 to 50 characters")
        @JsonProperty("title")
        String title,

        @JsonProperty("description")
        String description,

        @Size(max = RococoGatewayServiceConfig.ONE_MB)
        @JsonProperty("photo")
        String photo,

        @JsonProperty("geo")
        GeoJson geo) {

    public static @Nonnull MuseumJson fromGrpcMessage(@Nonnull MuseumResponse museum) {
        return new MuseumJson(
                UUID.fromString(museum.getId()),
                museum.getTitle(),
                museum.getDescription(),
                convertPhotoFromBase64(museum.getPhoto()),
                GeoJson.fromGrpcMessage(museum.getGeo())
        );
    }

    private static String convertPhotoFromBase64(ByteString photo) {
        if (photo == null || photo.isEmpty()) {
            return "";
        }
        return photo.toStringUtf8();
    }
}
