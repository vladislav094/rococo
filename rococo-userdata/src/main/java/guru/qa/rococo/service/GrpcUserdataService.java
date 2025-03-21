package guru.qa.rococo.service;

import com.google.protobuf.ByteString;
import guru.qa.rococo.data.UserEntity;
import guru.qa.rococo.data.repository.UserRepository;
import guru.qa.rococo.ex.NotFoundException;
import guru.qa.rococo.grpc.GetCurrentUserRequest;
import guru.qa.rococo.grpc.RococoUserdataServiceGrpc;
import guru.qa.rococo.grpc.UserdataGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@GrpcService
public class GrpcUserdataService extends RococoUserdataServiceGrpc.RococoUserdataServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcUserdataService.class);

    private final UserRepository userRepository;

    @Autowired
    public GrpcUserdataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getCurrentUser(GetCurrentUserRequest request, StreamObserver<UserdataGrpc> responseObserver) {
        final String username = request.getUsername();
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username: " + username + " not found."));
        responseObserver.onNext(buildResponse(userEntity));
        responseObserver.onCompleted();
    }

    @Transactional
    public void updateUser(UserdataGrpc request, StreamObserver<UserdataGrpc> responseObserver) {
        final String username = request.getUsername();
        final UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User with username: " + username + " not found."));
        userEntity.setFirstname(request.getFirstname());
        userEntity.setLastname(request.getLastname());
        userEntity.setAvatar(request.getAvatar().toByteArray());

        responseObserver.onNext(buildResponse(userRepository.save(userEntity)));
        responseObserver.onCompleted();
    }


    private static UserdataGrpc buildResponse(UserEntity entity) {
        return UserdataGrpc.newBuilder()
                .setId(entity.getId().toString())
                .setUsername(entity.getUsername())
                .setFirstname(entity.getFirstname() != null ? entity.getFirstname() : "")
                .setLastname(entity.getLastname() != null ? entity.getLastname() : "")
                .setAvatar(entity.getAvatar() != null ?
                        ByteString.copyFrom(entity.getAvatar()) : ByteString.EMPTY)
                .build();
    }
//
//    @Transactional
//    @Override
//    public void createMuseum(CreateMuseumRequest request, StreamObserver<MuseumResponse> responseObserver) {
//        try {
//            validateRequest(request);
//            GeoEntity geoEntity = processGeoEntity(request.getCity(), request.getCountryId());
//            MuseumEntity museumEntity = new MuseumEntity();
//            museumEntity.setTitle(request.getTitle());
//            museumEntity.setDescription(request.getDescription());
//            museumEntity.setPhoto(request.getPhoto().toByteArray());
//            museumEntity.setGeo(geoEntity);
//
//            responseObserver.onNext(buildResponse(museumRepository.save(museumEntity)));
//            responseObserver.onCompleted();
//        } catch (Exception e) {
//            GrpcExceptionHandler.handleException(responseObserver, e);
//        }
//    }
//
//    @Transactional
//    @Override
//    public void updateMuseum(UpdateMuseumRequest request, StreamObserver<MuseumResponse> responseObserver) {
//        try {
//            validateRequest(request);
//            MuseumEntity museumEntity = findMuseumById(request.getId());
//            updateMuseumData(museumEntity, request);
//            GeoEntity geoEntity = processGeoEntity(request.getCity(), request.getCountryId());
//            museumEntity.setGeo(geoEntity);
//
//            responseObserver.onNext(buildResponse(museumRepository.save(museumEntity)));
//            responseObserver.onCompleted();
//        } catch (Exception e) {
//            GrpcExceptionHandler.handleException(responseObserver, e);
//        }
//    }
//
//    private void validateRequest(Object request) {
//        if (request == null) {
//            throw new IllegalArgumentException("Request or museum data is null");
//        }
//    }
//
//    private MuseumEntity findMuseumById(String id) {
//        return museumRepository.findById(UUID.fromString(id))
//                .orElseThrow(() -> new MuseumNotFoundException("Museum with id: " + id + " not found."));
//    }
//
//    private void updateMuseumData(MuseumEntity museumEntity, UpdateMuseumRequest request) {
//
//        museumEntity.setTitle(request.getTitle());
//        museumEntity.setDescription(request.getDescription());
//        museumEntity.setPhoto(request.getPhoto().toByteArray());
//    }
//
//    private GeoEntity processGeoEntity(String city, String countryId) {
//        Optional<GeoEntity> existingGeoEntity = geoRepository.findByCity(city);
//        if (existingGeoEntity.isPresent()) {
//            GeoEntity geoEntity = existingGeoEntity.get();
//            if (!geoEntity.getCountry().getId().equals(UUID.fromString(countryId))) {
//                throw new IllegalArgumentException("City: " + city + " is already associated with another country.");
//            }
//            return geoEntity;
//        } else {
//            return createNewGeoEntity(city, countryId);
//        }
//    }
//
//    private GeoEntity createNewGeoEntity(String city, String countryId) {
//        GeoEntity geoEntity = new GeoEntity();
//        geoEntity.setCity(city);
//        CountryEntity countryEntity = countryRepository.findById(UUID.fromString(countryId))
//                .orElseThrow(() -> new CountryNotFoundException("Country with id: " + countryId + " not found."));
//        geoEntity.setCountry(countryEntity);
//        return geoRepository.save(geoEntity);
//    }
}
