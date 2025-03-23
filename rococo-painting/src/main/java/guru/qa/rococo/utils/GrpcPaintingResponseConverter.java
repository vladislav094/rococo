package guru.qa.rococo.utils;

import com.google.protobuf.ByteString;
import guru.qa.rococo.data.PaintingEntity;
import guru.qa.rococo.grpc.MuseumResponse;
import guru.qa.rococo.grpc.MuseumsResponse;
import guru.qa.rococo.grpc.PaintingResponse;
import guru.qa.rococo.grpc.PaintingsResponse;
import org.springframework.data.domain.Page;

public class GrpcPaintingResponseConverter {

    public static PaintingsResponse toGrpcPaintingsResponse(Page<PaintingEntity> museumPage) {
        return PaintingsResponse.newBuilder()
                .addAllPainting(museumPage.getContent().stream()
                        .map(GrpcPaintingResponseConverter::toGrpcPaintingResponse)
                        .toList())
                .setTotalPages(museumPage.getTotalPages())
                .setTotalElements(museumPage.getTotalElements())
                .build();
    }

    public static PaintingResponse toGrpcPaintingResponse(PaintingEntity museumEntity) {
        return PaintingResponse.newBuilder()
                .setId(museumEntity.getId().toString())
                .setTitle(museumEntity.getTitle())
                .setDescription(museumEntity.getDescription())
//                .setArtist(museumEntity.g)
//                .setMuseum(museumEntity.getMuseumId() != null ? museumEntity.getMuseumId() : null)
//                .setTitle(museumEntity.getTitle())
//                .setDescription(museumEntity.getDescription())
//                .setPhoto(museumEntity.getPhoto() != null ? ByteString.copyFrom(museumEntity.getPhoto()) : ByteString.EMPTY)
//                .setGeo(toGrpcGeo(museumEntity.getGeo()))
                .build();
    }
}
