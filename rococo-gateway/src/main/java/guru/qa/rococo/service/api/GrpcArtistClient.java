package guru.qa.rococo.service.api;

import com.google.protobuf.ByteString;
import guru.qa.rococo.grpc.*;
import guru.qa.rococo.model.ArtistJson;
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
public class GrpcArtistClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcArtistClient.class);

    @GrpcClient("artistGrpcClient")
    private RococoArtistServiceGrpc.RococoArtistServiceBlockingStub rococoArtistServiceStub;

    public @Nonnull Page<ArtistJson> getArtist(Pageable pageable, String name) {
        try {
            // Создаем gRPC запрос
            PageableRequest.Builder requestBuilder = PageableRequest.newBuilder()
                    .setPage(pageable.getPageNumber())
                    .setSize(pageable.getPageSize());
            // Добавляем name, если он передан
            if (name != null && !name.isEmpty()) {
                requestBuilder.setTitle(name);
            }
            // Выполняем gRPC запрос
            ArtistsResponse response = name != null && !name.isEmpty()
                    ? rococoArtistServiceStub.getArtistByName(requestBuilder.build())
                    : rococoArtistServiceStub.getArtists(requestBuilder.build());
            // Преобразуем ответ в List<ArtistJson>
            List<ArtistJson> museums = response.getArtistList()
                    .stream()
                    .map(ArtistJson::fromGrpcMessage)
                    .toList();
            // Возвращаем Page<ArtistJson>
            return new PageImpl<>(museums, pageable, response.getTotalElements());
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull ArtistJson getArtistById(String id) {
        try {
            ByIdRequest request = ByIdRequest.newBuilder()
                    .setId(id)
                    .build();
            return ArtistJson.fromGrpcMessage(rococoArtistServiceStub.getArtistById(request));
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull ArtistJson createArtist(ArtistJson artistJson) {
        try {
            // создаем CreateArtistRequest из тела REST запроса
            CreateArtistRequest request = CreateArtistRequest.newBuilder()
                    .setName(artistJson.name())
                    .setBiography(artistJson.biography())
                    .setPhoto(ByteString.copyFromUtf8(artistJson.photo()))
                    .build();
            // вызываем gRPC-сервис
            ArtistResponse response = rococoArtistServiceStub.createArtist(request);
            // преобразуем ответ в ArtistJson
            return ArtistJson.fromGrpcMessage(response);
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull ArtistJson updateArtist(ArtistJson artistJson) {
        try {
            // создаем UpdateArtistRequest из тела REST запроса
            UpdateArtistRequest request = UpdateArtistRequest.newBuilder()
                    .setId(artistJson.id().toString())
                    .setName(artistJson.name())
                    .setBiography(artistJson.biography())
                    .setPhoto(ByteString.copyFromUtf8(artistJson.photo()))
                    .build();
            // вызываем gRPC-сервис
            ArtistResponse response = rococoArtistServiceStub.updateArtist(request);
            // преобразуем ответ в ArtistJson
            return ArtistJson.fromGrpcMessage(response);
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

}
