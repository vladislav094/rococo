package guru.qa.rococo.service;

import guru.qa.rococo.data.ArtistEntity;
import guru.qa.rococo.data.repository.ArtistRepository;
import guru.qa.rococo.ex.ArtistNotFoundException;
import guru.qa.rococo.grpc.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static guru.qa.rococo.utils.GrpcArtistResponseConverter.toGrpcArtistResponse;
import static guru.qa.rococo.utils.GrpcArtistResponseConverter.toGrpcArtistsResponse;

@GrpcService
public class GrpcArtistService extends RococoArtistServiceGrpc.RococoArtistServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcArtistService.class);

    private final ArtistRepository artistRepository;

    @Autowired
    public GrpcArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void getArtists(PageableRequest request, StreamObserver<ArtistsResponse> responseObserver) {
        final Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<ArtistEntity> artistEntityPage = artistRepository.findAll(pageable);
        ArtistsResponse response = toGrpcArtistsResponse(artistEntityPage);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @Transactional(readOnly = true)
    public void getArtistByName(PageableRequest request, StreamObserver<ArtistsResponse> responseObserver) {
        final Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<ArtistEntity> artistEntityPage = artistRepository.findByName(request.getTitle(), pageable);
        ArtistsResponse response = toGrpcArtistsResponse(artistEntityPage);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Transactional(readOnly = true)
    @Override
    public void getArtistById(ByIdRequest request, StreamObserver<ArtistResponse> responseObserver) {
        ArtistEntity artistEntity = findArtistById(request.getId());
        responseObserver.onNext(toGrpcArtistResponse(artistEntity));
        responseObserver.onCompleted();
    }

    @Transactional
    @Override
    public void createArtist(CreateArtistRequest request, StreamObserver<ArtistResponse> responseObserver) {
        validateRequest(request);
        ArtistEntity museumEntity = new ArtistEntity();
        museumEntity.setName(request.getName());
        museumEntity.setBiography(request.getBiography());
        museumEntity.setPhoto(request.getPhoto().toByteArray());

        responseObserver.onNext(toGrpcArtistResponse(artistRepository.save(museumEntity)));
        responseObserver.onCompleted();
    }

    @Transactional
    @Override
    public void updateArtist(UpdateArtistRequest request, StreamObserver<ArtistResponse> responseObserver) {
        validateRequest(request);
        ArtistEntity artistEntity = findArtistById(request.getId());
        artistEntity.setName(request.getName());
        artistEntity.setBiography(request.getBiography());
        artistEntity.setPhoto(request.getPhoto().toByteArray());

        responseObserver.onNext(toGrpcArtistResponse(artistRepository.save(artistEntity)));
        responseObserver.onCompleted();
    }

    private void validateRequest(Object request) {
        if (request == null) {
            throw new IllegalArgumentException("Request or artist data is null");
        }
    }

    private ArtistEntity findArtistById(String id) {
        return artistRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new ArtistNotFoundException("Artist with id: " + id + " not found."));
    }
}
