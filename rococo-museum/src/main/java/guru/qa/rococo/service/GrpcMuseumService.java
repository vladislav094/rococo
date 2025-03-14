package guru.qa.rococo.service;

import guru.qa.rococo.data.MuseumEntity;
import guru.qa.rococo.data.repository.MuseumRepository;
import guru.qa.rococo.ex.MuseumNotFoundException;
import guru.qa.rococo.grpc.*;
import guru.qa.rococo.utils.GrpcResponseConverter;
import io.grpc.Status;
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

import static guru.qa.rococo.utils.GrpcResponseConverter.buildResponse;

@GrpcService
public class GrpcMuseumService extends RococoMuseumServiceGrpc.RococoMuseumServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcMuseumService.class);

    private final MuseumRepository museumRepository;

    @Autowired
    public GrpcMuseumService(MuseumRepository museumRepository) {
        this.museumRepository = museumRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public void getMuseums(PageableRequest request, StreamObserver<MuseumsResponse> responseObserver) {
        final Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<MuseumEntity> museumPage;
        request.getTitle();
        if (!request.getTitle().isEmpty()) {
            museumPage = museumRepository.findByTitle(request.getTitle(), pageable);
        } else {
            museumPage = museumRepository.findAll(pageable);
        }
        MuseumsResponse response = MuseumsResponse.newBuilder()
                .addAllMuseum(museumPage.getContent().stream()
                        .map(GrpcResponseConverter::toGrpcMuseum)
                        .toList())
                .setTotalPages(museumPage.getTotalPages())
                .setTotalElements(museumPage.getTotalElements())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Transactional(readOnly = true)
    @Override
    public void getMuseumById(ByIdRequest request, StreamObserver<MuseumResponse> responseObserver) {
        try {
            UUID museumId = UUID.fromString(request.getId());
            final MuseumEntity museumEntity = museumRepository.findById(museumId)
                    .orElseThrow(() -> new MuseumNotFoundException("Museum with id: " + request.getId() + " not found."));
            responseObserver.onNext(buildResponse(museumEntity));
            responseObserver.onCompleted();
        } catch (IllegalArgumentException e) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("Invalid museum ID format: " + request.getId())
                    .asRuntimeException());
        } catch (MuseumNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }
    }
}
