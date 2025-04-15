package guru.qa.rococo.utils;

import com.google.protobuf.ByteString;
import guru.qa.rococo.data.CountryEntity;
import guru.qa.rococo.data.GeoEntity;
import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.grpc.CountryResponse;
import guru.qa.rococo.grpc.GeoResponse;
import guru.qa.rococo.grpc.MuseumResponse;
import guru.qa.rococo.grpc.MuseumsResponse;
import org.springframework.data.domain.Page;

public class GrpcResponseConverter {

    public static MuseumsResponse toGrpcMuseumsResponse(Page<MuseumEntity> museumPage) {
        return MuseumsResponse.newBuilder()
                .addAllMuseum(museumPage.getContent().stream()
                        .map(GrpcResponseConverter::toGrpcMuseumResponse)
                        .toList())
                .setTotalPages(museumPage.getTotalPages())
                .setTotalElements(museumPage.getTotalElements())
                .build();
    }

    public static MuseumResponse toGrpcMuseumResponse(MuseumEntity museumEntity) {
        return MuseumResponse.newBuilder()
                .setId(museumEntity.getId().toString())
                .setTitle(museumEntity.getTitle())
                .setDescription(museumEntity.getDescription())
                .setPhoto(museumEntity.getPhoto() != null ? ByteString.copyFrom(museumEntity.getPhoto()) : ByteString.EMPTY)
                .setGeo(toGrpcGeo(museumEntity.getGeo()))
                .build();
    }

    public static GeoResponse toGrpcGeo(GeoEntity geoEntity) {
        return GeoResponse.newBuilder()
                .setId(geoEntity.getId().toString())
                .setCity(geoEntity.getCity())
                .setCountry(toGrpcCountry(geoEntity.getCountry()))
                .build();
    }

    public static CountryResponse toGrpcCountry(CountryEntity countryEntity) {
        return CountryResponse.newBuilder()
                .setId(countryEntity.getId().toString())
                .setName(countryEntity.getName())
                .build();
    }
}
