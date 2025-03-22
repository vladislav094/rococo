package guru.qa.rococo.utils;

import com.google.protobuf.ByteString;
import guru.qa.rococo.data.PaintingEntity;
import guru.qa.rococo.grpc.MuseumResponse;
import guru.qa.rococo.grpc.MuseumsResponse;
import guru.qa.rococo.grpc.PaintingResponse;
import org.springframework.data.domain.Page;

public class GrpcPaintingResponseConverter {

//    public static MuseumsResponse toGrpcPaintingsResponse(Page<PaintingEntity> museumPage) {
//        return MuseumsResponse.newBuilder()
//                .addAllMuseum(museumPage.getContent().stream()
//                        .map(GrpcPaintingResponseConverter::toGrpcMuseumResponse)
//                        .toList())
//                .setTotalPages(museumPage.getTotalPages())
//                .setTotalElements(museumPage.getTotalElements())
//                .build();
//    }
//
//    public static PaintingResponse toGrpcPaintingResponse(PaintingEntity museumEntity) {
//        return PaintingResponse.newBuilder()
//                .setId(museumEntity.getId().toString())
//
//
//                .setTitle(museumEntity.getTitle())
//                .setDescription(museumEntity.getDescription())
//                .setPhoto(museumEntity.getPhoto() != null ? ByteString.copyFrom(museumEntity.getPhoto()) : ByteString.EMPTY)
//                .setGeo(toGrpcGeo(museumEntity.getGeo()))
//                .build();
//    }
}
