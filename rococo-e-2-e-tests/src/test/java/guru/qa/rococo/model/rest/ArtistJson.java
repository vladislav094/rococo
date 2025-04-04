package guru.qa.rococo.model.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import guru.qa.rococo.data.entity.artist.ArtistEntity;
import guru.qa.rococo.grpc.ArtistResponse;

import javax.annotation.Nonnull;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static guru.qa.rococo.utils.ImageUtils.convertPhotoFromBase64;

public record ArtistJson(
        @JsonProperty("id")
        UUID id,

        @JsonProperty("name")
        String name,

        @JsonProperty("biography")
        String biography,

        @JsonProperty("photo")
        String photo) {

    public ArtistJson(@Nonnull UUID id) {
        this(id, null, null, null);
    }

    public ArtistJson(UUID id, String name, String biography, String photo) {
        this.id = id;
        this.name = name;
        this.biography = biography;
        this.photo = photo;
    }

    public static @Nonnull ArtistJson fromGrpcMessage(@Nonnull ArtistResponse artist) {
        return new ArtistJson(
                UUID.fromString(artist.getId()),
                artist.getName(),
                artist.getBiography(),
                convertPhotoFromBase64(artist.getPhoto())
        );
    }

    public static ArtistJson fromEntity(ArtistEntity artistEntity) {
        return new ArtistJson(
                artistEntity.getId(),
                artistEntity.getName(),
                artistEntity.getBiography(),
                new String(artistEntity.getPhoto(), StandardCharsets.UTF_8)
        );
    }

}
