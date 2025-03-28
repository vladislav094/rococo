package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.rococo.config.RococoGatewayServiceConfig;
import guru.qa.rococo.grpc.ArtistResponse;
import guru.qa.rococo.grpc.MuseumResponse;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ArtistJson(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("name")
        @Size(max = 255, message = "Name can`t be longer than 255 characters")
        String name,

        @JsonProperty("biography")
        @Size(max = 2000, message = "Biography can`t be longer than 2000 characters")
        String biography,

        @Size(max = RococoGatewayServiceConfig.ONE_MB)
        @JsonProperty("photo")
        String photo) {

        public static @Nonnull ArtistJson fromGrpcMessage(@Nonnull ArtistResponse artist) {
                return new ArtistJson(
                        UUID.fromString(artist.getId()),
                        artist.getName(),
                        artist.getBiography(),
                        convertPhotoFromBase64(artist.getPhoto())
                );
        }

        private static String convertPhotoFromBase64(ByteString photo) {
                if (photo == null || photo.isEmpty()) {
                        return "";
                }
                return photo.toStringUtf8();
        }
}
