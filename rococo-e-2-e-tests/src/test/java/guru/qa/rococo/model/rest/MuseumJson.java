package guru.qa.rococo.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.rococo.data.entity.museum.MuseumEntity;
import guru.qa.rococo.grpc.MuseumResponse;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

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

    public static @Nonnull MuseumJson fromEntity(MuseumEntity museumEntity) {
        return new MuseumJson(
                museumEntity.getId(),
                museumEntity.getTitle(),
                museumEntity.getDescription(),
                museumEntity.getPhoto() != null
                        ? new String(museumEntity.getPhoto(), StandardCharsets.UTF_8)
                        : null,
                new GeoJson(
                        museumEntity.getGeo().getId(),
                        museumEntity.getGeo().getCity(),
                        new CountryJson(
                                museumEntity.getGeo().getCountry().getId(),
                                museumEntity.getGeo().getCountry().getName()
                        )
                )
        );
    }
}
