package guru.qa.rococo.service;

import guru.qa.rococo.data.CountryEntity;
import guru.qa.rococo.data.GeoEntity;
import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.data.repository.CountryRepository;
import guru.qa.rococo.data.repository.GeoRepository;
import guru.qa.rococo.data.repository.MuseumRepository;
import guru.qa.rococo.ex.CountryNotFoundException;
import guru.qa.rococo.ex.MuseumNotFoundException;
import guru.qa.rococo.grpc.*;
import guru.qa.rococo.utils.GrpcResponseConverter;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.rococo.utils.GrpcResponseConverter.buildMuseumsResponse;

@GrpcService
public class GrpcMuseumService extends RococoMuseumServiceGrpc.RococoMuseumServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcMuseumService.class);

    private final MuseumRepository museumRepository;
    private final CountryRepository countryRepository;
    private final GeoRepository geoRepository;

    @Autowired
    public GrpcMuseumService(MuseumRepository museumRepository, CountryRepository countryRepository, GeoRepository geoRepository) {
        this.museumRepository = museumRepository;
        this.countryRepository = countryRepository;
        this.geoRepository = geoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void getMuseums(PageableRequest request, StreamObserver<MuseumsResponse> responseObserver) {
        final Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<MuseumEntity> museumPage = museumRepository.findAll(pageable);
        MuseumsResponse response = buildMuseumsResponse(museumPage);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @Transactional(readOnly = true)
    public void getMuseumByTitle(PageableRequest request, StreamObserver<MuseumsResponse> responseObserver) {
        final Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<MuseumEntity> museumPage = museumRepository.findByTitle(request.getTitle(), pageable);
        MuseumsResponse response = buildMuseumsResponse(museumPage);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Transactional(readOnly = true)
    @Override
    public void getMuseumById(ByIdRequest request, StreamObserver<MuseumResponse> responseObserver) {
        try {
            final MuseumEntity museumEntity = findMuseumById(request.getId());
            responseObserver.onNext(GrpcResponseConverter.buildMuseumResponse(museumEntity));
            responseObserver.onCompleted();
        } catch (Exception e) {
            GrpcExceptionHandler.handleException(responseObserver, e);
        }
    }

    @Transactional
    @Override
    public void createMuseum(CreateMuseumRequest request, StreamObserver<MuseumResponse> responseObserver) {
        try {
            validateRequest(request);
            GeoEntity geoEntity = processGeoEntity(request.getCity(), request.getCountryId());
            MuseumEntity museumEntity = new MuseumEntity();
            museumEntity.setTitle(request.getTitle());
            museumEntity.setDescription(request.getDescription());
            museumEntity.setPhoto(request.getPhoto().toByteArray());
            museumEntity.setGeo(geoEntity);

            responseObserver.onNext(GrpcResponseConverter.buildMuseumResponse(museumRepository.save(museumEntity)));
            responseObserver.onCompleted();
        } catch (Exception e) {
            GrpcExceptionHandler.handleException(responseObserver, e);
        }
    }

    @Transactional
    @Override
    public void updateMuseum(UpdateMuseumRequest request, StreamObserver<MuseumResponse> responseObserver) {
        try {
            validateRequest(request);
            MuseumEntity museumEntity = findMuseumById(request.getId());
            updateMuseumData(museumEntity, request);
            GeoEntity geoEntity = processGeoEntity(request.getCity(), request.getCountryId());
            museumEntity.setGeo(geoEntity);

            responseObserver.onNext(GrpcResponseConverter.buildMuseumResponse(museumRepository.save(museumEntity)));
            responseObserver.onCompleted();
        } catch (Exception e) {
            GrpcExceptionHandler.handleException(responseObserver, e);
        }
    }

    private void validateRequest(Object request) {
        if (request == null) {
            throw new IllegalArgumentException("Request or museum data is null");
        }
    }

    private MuseumEntity findMuseumById(String id) {
        return museumRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new MuseumNotFoundException("Museum with id: " + id + " not found."));
    }

    private void updateMuseumData(MuseumEntity museumEntity, UpdateMuseumRequest request) {

        museumEntity.setTitle(request.getTitle());
        museumEntity.setDescription(request.getDescription());
        museumEntity.setPhoto(request.getPhoto().toByteArray());
    }

    private GeoEntity processGeoEntity(String city, String countryId) {
        Optional<GeoEntity> existingGeoEntity = geoRepository.findByCity(city);
        if (existingGeoEntity.isPresent()) {
            GeoEntity geoEntity = existingGeoEntity.get();
            if (!geoEntity.getCountry().getId().equals(UUID.fromString(countryId))) {
                throw new IllegalArgumentException("City: " + city + " is already associated with another country.");
            }
            return geoEntity;
        } else {
            return createNewGeoEntity(city, countryId);
        }
    }

    private GeoEntity createNewGeoEntity(String city, String countryId) {
        GeoEntity geoEntity = new GeoEntity();
        geoEntity.setCity(city);
        CountryEntity countryEntity = countryRepository.findById(UUID.fromString(countryId))
                .orElseThrow(() -> new CountryNotFoundException("Country with id: " + countryId + " not found."));
        geoEntity.setCountry(countryEntity);
        return geoRepository.save(geoEntity);
    }
}
