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

    @Transactional(readOnly = true)
    public void getAllMuseums(PageableRequest request, StreamObserver<MuseumsResponse> responseObserver) {
        // данные с пагинацией
        Page<MuseumEntity> museumPage = museumRepository.findAll(
                PageRequest.of(request.getPage(), request.getSize())
        );
        // результат в gRPC ответ
        MuseumsResponse response = MuseumsResponse.newBuilder()
                .addAllAllMuseum(museumPage.getContent().stream()
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
            final MuseumEntity museumEntity = museumRepository.findById(UUID.fromString(request.getId()))
                    .orElseThrow(() -> new MuseumNotFoundException("Museum with id: " + request.getId() + " not found."));
            responseObserver.onNext(buildResponse(museumEntity));
            responseObserver.onCompleted();
        } catch (MuseumNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void getMuseumByTitle(ByTitleRequest request, StreamObserver<MuseumResponse> responseObserver) {
        try {
            final MuseumEntity museumEntity = museumRepository.findByTitle(request.getTitle())
                    .orElseThrow(() -> new MuseumNotFoundException("Museum with title: " + request.getTitle() + " not found."));
            responseObserver.onNext(buildResponse(museumEntity));
            responseObserver.onCompleted();
        } catch (MuseumNotFoundException e) {
            responseObserver.onError(Status.NOT_FOUND.
                    withDescription(e.getMessage())
                    .asRuntimeException());
        }
    }
}
