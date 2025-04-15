package guru.qa.rococo.utils;

import com.google.protobuf.ByteString;
import guru.qa.rococo.data.ArtistEntity;
import guru.qa.rococo.grpc.ArtistResponse;
import guru.qa.rococo.grpc.ArtistsResponse;
import org.springframework.data.domain.Page;

public final class GrpcArtistResponseConverter {

    public static ArtistResponse toGrpcArtistResponse(ArtistEntity entity) {
        return ArtistResponse.newBuilder()
                .setId(entity.getId().toString())
                .setName(entity.getName())
                .setBiography(entity.getBiography())
                .setPhoto(entity.getPhoto() != null ? ByteString.copyFrom(entity.getPhoto()) : ByteString.EMPTY)
                .build();
    }

    public static ArtistsResponse toGrpcArtistsResponse(Page<ArtistEntity> entityPage) {
        return ArtistsResponse.newBuilder()
                .addAllArtist(entityPage.getContent().stream()
                        .map(GrpcArtistResponseConverter::toGrpcArtistResponse)
                        .toList())
                .setTotalPages(entityPage.getTotalPages())
                .setTotalElements(entityPage.getTotalElements())
                .build();
    }
}
