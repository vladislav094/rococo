package guru.qa.rococo.service;

import com.google.protobuf.ByteString;
import guru.qa.rococo.clients.GrpcArtistClient;
import guru.qa.rococo.clients.GrpcMuseumClient;
import guru.qa.rococo.data.PaintingEntity;
import guru.qa.rococo.data.repository.PaintingRepository;
import guru.qa.rococo.ex.PaintingNotFoundException;
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
import java.util.concurrent.CompletableFuture;

import static guru.qa.rococo.utils.GrpcPaintingResponseConverter.toGrpcPaintingsResponse;

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
        final Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<PaintingEntity> museumPage = paintingRepository.findAll(pageable);
        PaintingsResponse response = toGrpcPaintingsResponse(museumPage);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    @Transactional(readOnly = true)
    public void getPaintingByTitle(PageableRequest request, StreamObserver<PaintingsResponse> responseObserver) {
        final Pageable pageable = PageRequest.of(request.getPage(), request.getSize());
        Page<PaintingEntity> museumPage = paintingRepository.findByTitle(request.getTitle(), pageable);
        PaintingsResponse response = toGrpcPaintingsResponse(museumPage);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getPaintingById(ByIdRequest request, StreamObserver<PaintingResponse> responseObserver) {
        final PaintingEntity paintingEntity = findPaintingById(request.getId());
        final String artistId = paintingEntity.getArtistId().toString();
        final String museumId = paintingEntity.getMuseumId().toString();

        // Логирование для диагностики
        LOG.info("Try get painting {}, with artistId: {}, museumId: {}", paintingEntity.getId(), artistId, museumId);
        // Асинхронный запрос к микросервису художника
        final CompletableFuture<ArtistResponse> artistFuture = grpcArtistClient.getArtistAsync(artistId);
        // Асинхронный запрос к микросервису музея (если museumId указан)
        final CompletableFuture<MuseumResponse> museumFuture;
        if (!museumId.isEmpty()) {
            LOG.info("Fetching museum by ID: {}", museumId);
            museumFuture = grpcMuseumClient.getMuseumAsync(museumId);
        } else {
            LOG.info("Museum ID not provided, skipping museum fetch");
            museumFuture = CompletableFuture.completedFuture(null); // Если museumId не указан, возвращаем null
        }

        // Ожидание завершения всех запросов
        CompletableFuture.allOf(artistFuture, museumFuture)
                .thenAccept(ignored -> {
                    try {

                        // Получение результатов запросов
                        ArtistResponse artistResponse = artistFuture.get();
                        MuseumResponse museumResponse = museumFuture.get(); // Может быть null, если museumId не указан

                        // Создание ответа
                        PaintingResponse.Builder responseBuilder = PaintingResponse.newBuilder()
                                .setId(paintingEntity.getId().toString())
                                .setTitle(paintingEntity.getTitle())
                                .setDescription(paintingEntity.getDescription())
                                .setArtist(artistResponse)
                                .setContent(paintingEntity.getContent() != null
                                        ? ByteString.copyFrom(paintingEntity.getContent())
                                        : ByteString.EMPTY
                                );
                        // Добавляем музей в ответ, только если он был указан
                        if (museumResponse != null) {
                            responseBuilder.setMuseum(museumResponse);
                        }

                        // Отправка ответа клиенту
                        responseObserver.onNext(responseBuilder.build());
                        responseObserver.onCompleted();
                    } catch (Exception e) {
                        // Обработка ошибок
                        LOG.error("Error while creating painting", e);
                        responseObserver.onError(e);
                    }
                })
                .exceptionally(ex -> {
                    // Обработка ошибок при выполнении запросов
                    LOG.error("Error while processing gRPC requests", ex);
                    responseObserver.onError(ex);
                    return null;
                });
    }

    @Transactional
    @Override
    public void createPainting(CreatePaintingRequest request, StreamObserver<PaintingResponse> responseObserver) {
        final String artistId = request.getArtistId();
        final String museumId = request.getMuseumId();

        // Логирование для диагностики
        LOG.info("Creating painting with artistId: {}, museumId: {}", artistId, museumId);

        // Асинхронный запрос к микросервису художника
        final CompletableFuture<ArtistResponse> artistFuture = grpcArtistClient.getArtistAsync(artistId);

        // Асинхронный запрос к микросервису музея (если museumId указан)
        final CompletableFuture<MuseumResponse> museumFuture;
        if (!museumId.isEmpty()) {
            LOG.info("Fetching museum by ID: {}", museumId);
            museumFuture = grpcMuseumClient.getMuseumAsync(museumId);
        } else {
            LOG.info("Museum ID not provided, skipping museum fetch");
            museumFuture = CompletableFuture.completedFuture(null); // Если museumId не указан, возвращаем null
        }

        // Ожидание завершения всех запросов
        CompletableFuture.allOf(artistFuture, museumFuture)
                .thenAccept(ignored -> {
                    try {
                        // Проверка данных
                        validateRequest(request);

                        // Получение результатов запросов
                        ArtistResponse artistResponse = artistFuture.get();
                        MuseumResponse museumResponse = museumFuture.get(); // Может быть null, если museumId не указан

                        // Создание сущности Painting
                        PaintingEntity paintingEntity = new PaintingEntity();
                        paintingEntity.setTitle(request.getTitle());
                        paintingEntity.setDescription(request.getDescription());
                        paintingEntity.setArtistId(UUID.fromString(artistId));

                        // Устанавливаем museumId только если он указан
                        if (!museumId.isEmpty()) {
                            paintingEntity.setMuseumId(UUID.fromString(museumId));
                        }

                        paintingEntity.setContent(request.getContent().toByteArray());

                        // Сохранение сущности в базу данных
                        PaintingEntity savedPainting = paintingRepository.save(paintingEntity);

                        // Создание ответа
                        PaintingResponse.Builder responseBuilder = PaintingResponse.newBuilder()
                                .setId(savedPainting.getId().toString())
                                .setTitle(savedPainting.getTitle())
                                .setDescription(savedPainting.getDescription())
                                .setArtist(artistResponse)
                                .setContent(savedPainting.getContent() != null
                                        ? ByteString.copyFrom(savedPainting.getContent())
                                        : ByteString.EMPTY
                                );
                        // Добавляем музей в ответ, только если он был указан
                        if (museumResponse != null) {
                            responseBuilder.setMuseum(museumResponse);
                        }

                        // Отправка ответа клиенту
                        responseObserver.onNext(responseBuilder.build());
                        responseObserver.onCompleted();
                    } catch (Exception e) {
                        // Обработка ошибок
                        LOG.error("Error while creating painting", e);
                        responseObserver.onError(e);
                    }
                })
                .exceptionally(ex -> {
                    // Обработка ошибок при выполнении запросов
                    LOG.error("Error while processing gRPC requests", ex);
                    responseObserver.onError(ex);
                    return null;
                });
    }

    private void validateRequest(Object request) {
        if (request == null) {
            throw new IllegalArgumentException("Request or painting data is null");
        }
    }

    private PaintingEntity findPaintingById(String id) {
        return paintingRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new PaintingNotFoundException("Painting with id: " + id + " not found."));
    }
}
