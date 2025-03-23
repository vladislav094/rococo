package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.rococo.config.RococoGatewayServiceConfig;
import guru.qa.rococo.grpc.PaintingResponse;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record PaintingJson(
        @JsonProperty("id")
        UUID id,

        @NotBlank(message = "Painting can not be blank")
        @Size(max = 255)
        @JsonProperty("title")
        String title,

        @Size(max = 1000)
        @JsonProperty("description")
        String description,

        @JsonProperty("artist")
        ArtistJson artist,

        @JsonProperty("museum")
        MuseumJson museum,

        @Size(max = RococoGatewayServiceConfig.ONE_MB)
        @JsonProperty("content")
        String content) {

    public static @Nonnull PaintingJson fromGrpcMessage(@Nonnull PaintingResponse painting) {
        return new PaintingJson(
                UUID.fromString(painting.getId()),
                painting.getTitle(),
                painting.getDescription(),
                painting.hasMuseum() ? ArtistJson.fromGrpcMessage(painting.getArtist()) : null,
                painting.hasMuseum() ? MuseumJson.fromGrpcMessage(painting.getMuseum()) : null,
                convertPhotoFromBase64(painting.getContent())
        );
    }

    private static String convertPhotoFromBase64(ByteString photo) {
        if (photo == null || photo.isEmpty()) {
            return "";
        }
        return photo.toStringUtf8();
    }
}
