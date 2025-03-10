package guru.qa.rococo.service;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import guru.qa.rococo.data.CountryEntity;
import guru.qa.rococo.data.GeoEntity;
import guru.qa.rococo.data.repository.MuseumRepository;
import guru.qa.rococo.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@GrpcService
public class GrpcMuseumService extends RococoMuseumServiceGrpc.RococoMuseumServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcMuseumService.class);

    private final MuseumRepository museumRepository;

    @Autowired
    public GrpcMuseumService(MuseumRepository museumRepository) {
        this.museumRepository = museumRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public void getAllMuseum(Empty request, StreamObserver<MuseumResponse> responseObserver) {
        MuseumResponse response = MuseumResponse.newBuilder()
                .addAllAllMuseum(museumRepository.findAll().stream()
                        .map(e -> Museum.newBuilder()
                                .setId(e.getId().toString())
                                .setTitle(e.getTitle())
                                .setDescription(e.getDescription())
                                .setPhoto(e.getPhoto() != null ? ByteString.copyFrom(e.getPhoto()) : ByteString.EMPTY)
                                .setGeo(toGrpcGeo(e.getGeo()))
                                .build())
                        .toList())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private Geo toGrpcGeo(GeoEntity geoEntity) {
        return Geo.newBuilder()
                .setId(geoEntity.getId().toString())
                .setCity(geoEntity.getCity())
                .setCountry(toGrpcCountry(geoEntity.getCountry()))
                .build();
    }

    private Country toGrpcCountry(CountryEntity countryEntity) {
        return Country.newBuilder()
                .setId(countryEntity.getId().toString())
                .setName(countryEntity.getName())
                .build();
    }
}
