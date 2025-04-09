package guru.qa.rococo.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.data.entity.painting.PaintingEntity;
import guru.qa.rococo.grpc.PaintingResponse;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static guru.qa.rococo.utils.ImageUtils.convertPhotoFromBase64;

public record PaintingJson(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("title")
        String title,

        @JsonProperty("description")
        String description,

        @JsonProperty("artist")
        ArtistJson artist,

        @JsonProperty("museum")
        MuseumJson museum,

        @JsonProperty("content")
        String content) {

    public static @Nonnull PaintingJson fromGrpcMessage(@Nonnull PaintingResponse painting) {
        return new PaintingJson(
                UUID.fromString(painting.getId()),
                painting.getTitle(),
                painting.getDescription(),
                ArtistJson.fromGrpcMessage(painting.getArtist()),
                MuseumJson.fromGrpcMessage(painting.getMuseum()),
                convertPhotoFromBase64(painting.getContent())
        );
    }

    public static @Nonnull PaintingJson fromEntity(@Nonnull PaintingEntity paintingEntity) {
        return new PaintingJson(
                paintingEntity.getId(),
                paintingEntity.getTitle(),
                paintingEntity.getDescription(),
                new ArtistJson(paintingEntity.getArtistId()),
                new MuseumJson(paintingEntity.getMuseumId()),
                new String(paintingEntity.getContent(), StandardCharsets.UTF_8)
        );
    }
}
