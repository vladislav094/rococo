package guru.qa.rococo.service.api;

import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import guru.qa.rococo.grpc.*;
import guru.qa.rococo.model.MuseumJson;
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
public class GrpcMuseumClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcMuseumClient.class);
    private static final Empty EMPTY = Empty.getDefaultInstance();

    @GrpcClient("museumGrpcClient")
    private RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub rococoMuseumServiceStub;

    public @Nonnull Page<MuseumJson> getMuseumsPage(Pageable pageable, String title) {
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
            MuseumsResponse response = title != null && !title.isEmpty()
                    ? rococoMuseumServiceStub.getMuseumByTitle(requestBuilder.build())
                    : rococoMuseumServiceStub.getMuseums(requestBuilder.build());
            // Преобразуем ответ в List<MuseumJson>
            List<MuseumJson> museums = response.getMuseumList()
                    .stream()
                    .map(MuseumJson::fromGrpcMessage)
                    .toList();
            // Возвращаем Page<MuseumJson>
            return new PageImpl<>(museums, pageable, response.getTotalElements());
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw handleStatusRuntimeException(e);
        }
    }

    public @Nonnull MuseumJson getMuseumById(String id) {
        try {
            ByIdRequest request = ByIdRequest.newBuilder()
                    .setId(id)
                    .build();
            return MuseumJson.fromGrpcMessage(rococoMuseumServiceStub.getMuseumById(request));
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw handleStatusRuntimeException(e);
        }
    }

    public @Nonnull MuseumJson createMuseum(MuseumJson museumJson) {
        try {
            CreateMuseumRequest request = CreateMuseumRequest.newBuilder()
                    .setTitle(museumJson.title())
                    .setDescription(museumJson.description())
                    .setPhoto(ByteString.copyFromUtf8(museumJson.photo()))
                    .setCity(museumJson.geo().city())
                    .setCountryId(museumJson.geo().country().id().toString())
                    .build();
            // вызываем gRPC-сервис
            MuseumResponse response = rococoMuseumServiceStub.createMuseum(request);
            // преобразуем ответ в MuseumJson
            return MuseumJson.fromGrpcMessage(response);
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw handleStatusRuntimeException(e);
        }
    }

    public @Nonnull MuseumJson updateMuseum(MuseumJson museumJson) {
        try {
            // создаем UpdateMuseumRequest из тела REST запроса
            UpdateMuseumRequest request = UpdateMuseumRequest.newBuilder()
                    .setId(museumJson.id().toString())
                    .setTitle(museumJson.title())
                    .setDescription(museumJson.description())
                    .setPhoto(ByteString.copyFromUtf8(museumJson.photo()))
                    .setCity(museumJson.geo().city())
                    .setCountryId(museumJson.geo().country().id().toString())
                    .build();
            // вызываем gRPC-сервис
            MuseumResponse response = rococoMuseumServiceStub.updateMuseum(request);
            // преобразуем ответ в MuseumJson
            return MuseumJson.fromGrpcMessage(response);
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw handleStatusRuntimeException(e);
        }
    }

    private ResponseStatusException handleStatusRuntimeException(StatusRuntimeException e) {
        HttpStatus httpStatus = switch (e.getStatus().getCode()) {
            case INVALID_ARGUMENT -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.SERVICE_UNAVAILABLE;
        };
        return new ResponseStatusException(httpStatus, e.getStatus().getDescription(), e);
    }
}
