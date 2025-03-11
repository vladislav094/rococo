package guru.qa.rococo.service.api;

import com.google.protobuf.Empty;
import guru.qa.rococo.grpc.MuseumsRequest;
import guru.qa.rococo.grpc.MuseumsResponse;
import guru.qa.rococo.grpc.RococoMuseumServiceGrpc;
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

    private static final Logger LOG = LoggerFactory.getLogger(GrpcCurrencyClient.class);
    private static final Empty EMPTY = Empty.getDefaultInstance();

    @GrpcClient("grpcMuseumClient")
    private RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub rococoMuseumServiceStub;

    public @Nonnull
    Page<MuseumJson> getAllMuseums(Pageable pageable) {
        try {
            // gRPC запрос с пагинацией
            MuseumsRequest request = MuseumsRequest.newBuilder()
                    .setPage(pageable.getPageNumber())
                    .setSize(pageable.getPageSize())
                    .build();

            // выполняем gRPC запрос
            MuseumsResponse response = rococoMuseumServiceStub.getAllMuseum(request);

            // преобразуем ответ в Page<MuseumJson>
            List<MuseumJson> museums = response.getAllMuseumList().stream()
                    .map(MuseumJson::fromGrpcMessage)
                    .toList();

            return new PageImpl<>(museums, pageable, response.getTotalElements());

        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }
}
