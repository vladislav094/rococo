package guru.qa.rococo.utils;

import com.google.protobuf.ByteString;
import guru.qa.rococo.data.CountryEntity;
import guru.qa.rococo.data.GeoEntity;
import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.grpc.CountryResponse;
import guru.qa.rococo.grpc.GeoResponse;
import guru.qa.rococo.grpc.MuseumResponse;

public class GrpcResponseConverter {

    public static MuseumResponse buildResponse(MuseumEntity entity) {
        return MuseumResponse.newBuilder()
                .setId(entity.getId().toString())
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setPhoto(entity.getPhoto() != null ?
                        ByteString.copyFrom(entity.getPhoto()) : ByteString.EMPTY)
                .setGeo(toGrpcGeo(entity.getGeo()))
                .build();
    }

    public static MuseumResponse toGrpcMuseum(MuseumEntity museumEntity) {
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
