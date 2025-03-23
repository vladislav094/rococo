package guru.qa.rococo.service.api;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import guru.qa.rococo.grpc.*;
import guru.qa.rococo.model.MuseumJson;
import guru.qa.rococo.model.PaintingJson;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class GrpcPaintingClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcPaintingClient.class);
    private static final Empty EMPTY = Empty.getDefaultInstance();

    @GrpcClient("paintingGrpcClient")
    private RococoPaintingServiceGrpc.RococoPaintingServiceBlockingStub rococoPaintingServiceStub;

    public @Nonnull Page<PaintingJson> getPaintingsPage(Pageable pageable, String title) {
        try {
            // Создаем gRPC запрос
            PageableRequest.Builder requestBuilder = PageableRequest.newBuilder()
                    .setPage(pageable.getPageNumber())
                    .setSize(pageable.getPageSize());
            // Добавляем title, если он передан
            if (title != null && !title.isEmpty()) {
                requestBuilder.setTitle(title);
            }
            // Выполняем gRPC запрос
            PaintingsResponse response = title != null && !title.isEmpty()
                    ? rococoPaintingServiceStub.getPaintingByTitle(requestBuilder.build())
                    : rococoPaintingServiceStub.getPaintings(requestBuilder.build());
            // Преобразуем ответ в List<MuseumJson>
            List<PaintingJson> museums = response.getPaintingList()
                    .stream()
                    .map(PaintingJson::fromGrpcMessage)
                    .toList();
            // Возвращаем Page<MuseumJson>
            return new PageImpl<>(museums, pageable, response.getTotalElements());
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull PaintingJson getPaintingById(String id) {
        try {
            ByIdRequest request = ByIdRequest.newBuilder()
                    .setId(id)
                    .build();
            return PaintingJson.fromGrpcMessage(rococoPaintingServiceStub.getPaintingById(request));
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull PaintingJson createPainting(PaintingJson paintingJson) {
        try {
            CreatePaintingRequest request = CreatePaintingRequest.newBuilder()
                    .setTitle(paintingJson.title())
                    .setDescription(paintingJson.description())
                    .setArtistId(paintingJson.artist().id().toString())
                    .setMuseumId(paintingJson.museum().id() != null ? paintingJson.museum().id().toString() : "")
                    .setContent(ByteString.copyFromUtf8(paintingJson.content()))
                    .build();
            // вызываем gRPC-сервис
            PaintingResponse response = rococoPaintingServiceStub.createPainting(request);
            // преобразуем ответ в MuseumJson
            return PaintingJson.fromGrpcMessage(response);
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }
}
