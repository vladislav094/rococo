package guru.qa.rococo.service;

import com.google.protobuf.ByteString;
import guru.qa.rococo.clients.GrpcArtistClient;
import guru.qa.rococo.clients.GrpcMuseumClient;
import guru.qa.rococo.data.PaintingEntity;
import guru.qa.rococo.data.repository.PaintingRepository;
import guru.qa.rococo.ex.PaintingNotFoundException;
import guru.qa.rococo.grpc.*;
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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;


@GrpcService
public class GrpcPaintingService extends RococoPaintingServiceGrpc.RococoPaintingServiceImplBase {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcPaintingService.class);
    private final PaintingRepository paintingRepository;
    private final GrpcMuseumClient grpcMuseumClient;
    private final GrpcArtistClient grpcArtistClient;

    @Autowired
    public GrpcPaintingService(PaintingRepository paintingRepository,
                               GrpcMuseumClient grpcMuseumClient,
                               GrpcArtistClient grpcArtistClient) {
        this.paintingRepository = paintingRepository;
        this.grpcMuseumClient = grpcMuseumClient;
        this.grpcArtistClient = grpcArtistClient;
    }

    @Override
    @Transactional(readOnly = true)
    public void getPaintings(PageableRequest request, StreamObserver<PaintingsResponse> responseObserver) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<PaintingEntity> paintingPage = paintingRepository.findAll(pageable);

        toGrpcPaintingsResponse(paintingPage)
                .whenComplete((response, ex) -> {
                    if (ex != null) {
                        handleError(responseObserver, "Error converting paintings", ex);
                    } else {
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    }
                });
    }

    @Override
    @Transactional(readOnly = true)
    public void getPaintingByTitle(PageableRequest request, StreamObserver<PaintingsResponse> responseObserver) {
        final Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<PaintingEntity> paintingPage = paintingRepository.findByTitle(request.getTitle(), pageable);

        toGrpcPaintingsResponse(paintingPage)
                .whenComplete((response, ex) -> {
                    if (ex != null) {
                        handleError(responseObserver, "Error converting paintings", ex);
                    } else {
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    }
                });
    }

    @Override
    @Transactional(readOnly = true)
    public void getPaintingById(ByIdRequest request, StreamObserver<PaintingResponse> responseObserver) {
        try {
            final PaintingEntity paintingEntity = findPaintingById(request.getId());
            final String artistId = paintingEntity.getArtistId().toString();
            final String museumId = paintingEntity.getMuseumId() != null ? paintingEntity.getMuseumId().toString() : null;
            LOG.info("Getting painting {}, with artistId: {}, museumId: {}", paintingEntity.getId(), artistId, museumId);

            final CompletableFuture<ArtistResponse> artistFuture = grpcArtistClient.getArtistAsync(artistId);   // Асинхронный запрос к микросервису artist
            final CompletableFuture<MuseumResponse> museumFuture = getMuseum(museumId);
            // Ожидание завершения всех запросов
            CompletableFuture.allOf(artistFuture, museumFuture)
                    .thenAccept(ignored -> {
                        try {
                            PaintingResponse response = paintingResponseBuilder(
                                    paintingEntity,
                                    artistFuture.get(),
                                    museumFuture.get() // Может быть null, если museumId не указан
                            );
                            responseObserver.onNext(response);
                            responseObserver.onCompleted();
                        } catch (Exception e) {
                            // Обработка ошибок
                            LOG.error("Error building painting response", e);
                            responseObserver.onError(e);
                        }
                    })
                    .exceptionally(ex -> {
                        // Обработка ошибок при выполнении запросов
                        LOG.error("Error while processing gRPC requests", ex);
                        responseObserver.onError(ex);
                        return null;
                    });
        } catch (Exception e) {
            handleError(responseObserver, "Error getting painting by ID", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public void getPaintingByAuthorId(PaintingByAuthorRequest request, StreamObserver<PaintingsResponse> responseObserver) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<PaintingEntity> paintingPage = paintingRepository.findPaintingEntitiesByArtistId(
                UUID.fromString(request.getAuthorId()), pageable
        );

        toGrpcPaintingsResponse(paintingPage)
                .whenComplete((response, ex) -> {
                    if (ex != null) {
                        handleError(responseObserver, "Error converting paintings", ex);
                    } else {
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    }
                });
    }

    @Override
    @Transactional
    public void createPainting(CreatePaintingRequest request, StreamObserver<PaintingResponse> responseObserver) {
        try {
            final String artistId = request.getArtistId();
            final String museumId = !request.getMuseumId().isEmpty() ? request.getMuseumId() : null;
            LOG.info("Creating painting with artistId: '{}', museumId: '{}'", artistId, museumId);
            // Асинхронный запрос к микросервису artist
            final CompletableFuture<ArtistResponse> artistFuture = grpcArtistClient.getArtistAsync(artistId);
            // Асинхронный запрос к микросервису museum (если museumId указан)
            final CompletableFuture<MuseumResponse> museumFuture = getMuseum(museumId);
            // Ожидание завершения всех запросов
            CompletableFuture.allOf(artistFuture, museumFuture)
                    .thenAccept(ignored -> {
                        try {
                            PaintingEntity paintingEntity = new PaintingEntity();
                            paintingEntity.setTitle(request.getTitle());
                            paintingEntity.setDescription(request.getDescription());
                            paintingEntity.setArtistId(UUID.fromString(artistId));
                            paintingEntity.setMuseumId(museumId != null ? UUID.fromString(museumId) : null);
                            paintingEntity.setContent(request.getContent().toByteArray());

                            PaintingResponse paintingResponse = paintingResponseBuilder(
                                    paintingRepository.save(paintingEntity),
                                    artistFuture.get(),
                                    museumFuture.get()  // Может быть null, если museumId не указан
                            );
                            responseObserver.onNext(paintingResponse);
                            responseObserver.onCompleted();
                        } catch (Exception e) {
                            handleError(responseObserver, "Error creating painting", e);
                        }
                    })
                    .exceptionally(ex -> {
                        handleError(responseObserver, "Error processing gRPC requests", ex);
                        return null;
                    });
        } catch (Exception e) {
            handleError(responseObserver, "Error in create painting", e);
        }
    }

    @Override
    @Transactional
    public void updatePainting(UpdatePaintingRequest request, StreamObserver<PaintingResponse> responseObserver) {
        try {
            final String artistId = request.getArtistId();
            final String museumId = !request.getMuseumId().isEmpty() ? request.getMuseumId() : null;
            LOG.info("Update painting '{}', set artistId: '{}', set: '{}'", request.getId(), artistId, museumId);
            // Асинхронный запрос к микросервису artist
            final CompletableFuture<ArtistResponse> artistFuture = grpcArtistClient.getArtistAsync(artistId);
            // Асинхронный запрос к микросервису museum (если museumId указан)
            final CompletableFuture<MuseumResponse> museumFuture = getMuseum(museumId);
            // Ожидание завершения всех запросов
            CompletableFuture.allOf(artistFuture, museumFuture)
                    .thenAccept(ignored -> {
                        try {
                            PaintingEntity paintingEntity = findPaintingById(request.getId());
                            paintingEntity.setTitle(request.getTitle());
                            paintingEntity.setDescription(request.getDescription());
                            paintingEntity.setArtistId(UUID.fromString(artistId));
                            paintingEntity.setMuseumId(museumId != null ? UUID.fromString(museumId) : null);
                            paintingEntity.setContent(request.getContent().toByteArray());

                            PaintingResponse paintingResponse = paintingResponseBuilder(
                                    paintingRepository.save(paintingEntity),
                                    artistFuture.get(),
                                    museumFuture.get()  // Может быть null, если museumId не указан
                            );
                            responseObserver.onNext(paintingResponse);
                            responseObserver.onCompleted();
                        } catch (Exception e) {
                            handleError(responseObserver, "Error creating painting", e);
                        }
                    })
                    .exceptionally(ex -> {
                        handleError(responseObserver, "Error processing gRPC requests", ex);
                        return null;
                    });
        } catch (Exception e) {
            handleError(responseObserver, "Error in create painting", e);
        }
    }

    private PaintingEntity findPaintingById(String id) {
        return paintingRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new PaintingNotFoundException("Painting with id: " + id + " not found."));
    }

    private PaintingResponse paintingResponseBuilder(PaintingEntity entity,
                                                     ArtistResponse artistResponse,
                                                     MuseumResponse museumResponse) {

        return PaintingResponse.newBuilder()
                .setId(entity.getId().toString())
                .setTitle(entity.getTitle())
                .setDescription(entity.getDescription())
                .setArtist(artistResponse)
                .setMuseum(museumResponse != null ? museumResponse : MuseumResponse.getDefaultInstance())
                .setContent(entity.getContent() != null ? ByteString.copyFrom(entity.getContent()) : ByteString.EMPTY)
                .build();
    }

    private CompletableFuture<PaintingsResponse> toGrpcPaintingsResponse(Page<PaintingEntity> page) {
        List<CompletableFuture<PaintingResponse>> paintingFutures = page.getContent()
                .stream()
                .map(this::toGrpcPaintingResponseAsync)
                .toList();

        return CompletableFuture.allOf(paintingFutures.toArray(new CompletableFuture[0]))
                .thenApply(ignored -> {
                    List<PaintingResponse> paintings = paintingFutures.stream()
                            .map(CompletableFuture::join)
                            .toList();

                    return PaintingsResponse.newBuilder()
                            .addAllPainting(paintings)
                            .setTotalPages(page.getTotalPages())
                            .setTotalElements(page.getTotalElements())
                            .build();
                });
    }

    private CompletableFuture<PaintingResponse> toGrpcPaintingResponseAsync(PaintingEntity entity) {
        CompletableFuture<ArtistResponse> artistFuture = grpcArtistClient.getArtistAsync(entity.getArtistId().toString());
        CompletableFuture<MuseumResponse> museumFuture = entity.getMuseumId() != null
                ? grpcMuseumClient.getMuseumAsync(entity.getMuseumId().toString())
                : CompletableFuture.completedFuture(null);

        return CompletableFuture.allOf(artistFuture, museumFuture)
                .thenApply(ignored -> {
                    try {
                        return PaintingResponse.newBuilder()
                                .setId(entity.getId().toString())
                                .setTitle(entity.getTitle())
                                .setDescription(entity.getDescription())
                                .setArtist(artistFuture.get())
                                .setMuseum(museumFuture.get() != null ? museumFuture.get() : MuseumResponse.getDefaultInstance())
                                .setContent(entity.getContent() != null
                                        ? ByteString.copyFrom(entity.getContent())
                                        : ByteString.EMPTY)
                                .build();
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                });
    }

    private CompletableFuture<MuseumResponse> getMuseum(String museumId) {
        if (museumId != null && !museumId.isEmpty()) {
            LOG.info("Fetching museum by ID: {}", museumId);
            return grpcMuseumClient.getMuseumAsync(museumId);
        } else {
            LOG.info("Museum ID not provided, skipping museum fetch");
            return CompletableFuture.completedFuture(null); // Если museumId не указан, возвращаем null
        }
    }

    private void handleError(StreamObserver<?> responseObserver, String message, Throwable ex) {
        LOG.error(message, ex);
        responseObserver.onError(Status.INTERNAL
                .withDescription(message + ": " + ex.getMessage())
                .asRuntimeException());
    }
}
