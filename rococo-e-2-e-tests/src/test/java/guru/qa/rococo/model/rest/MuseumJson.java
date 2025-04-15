package guru.qa.rococo.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import guru.qa.rococo.grpc.MuseumResponse;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static guru.qa.rococo.utils.ImageUtils.convertPhotoFromBase64;

public record MuseumJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("title")
        String title,
        @JsonProperty("description")
        String description,
        @JsonProperty("photo")
        String photo,
        @JsonProperty("geo")
        GeoJson geo) {

    public MuseumJson(@Nonnull UUID id) {
        this(id, null, null, null, null);
    }

    public MuseumJson(UUID id, String title, String description, String photo, GeoJson geo) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.photo = photo;
        this.geo = geo;
    }

    public static @Nonnull MuseumJson fromGrpcMessage(@Nonnull MuseumResponse museum) {
        return new MuseumJson(
                UUID.fromString(museum.getId()),
                museum.getTitle(),
                museum.getDescription(),
                convertPhotoFromBase64(museum.getPhoto()),
                GeoJson.fromGrpcMessage(museum.getGeo())
        );
    }

    public static @Nonnull MuseumJson fromEntity(MuseumEntity museumEntity) {
        return new MuseumJson(
                museumEntity.getId(),
                museumEntity.getTitle(),
                museumEntity.getDescription(),
                museumEntity.getPhoto() != null
                        ? new String(museumEntity.getPhoto(), StandardCharsets.UTF_8)
                        : null,
                GeoJson.fromEntity(museumEntity.getGeo())
        );
    }

}
