package guru.qa.rococo.service.api;

import com.google.protobuf.Empty;
import guru.qa.rococo.grpc.CountriesResponse;
import guru.qa.rococo.grpc.PageableRequest;
import guru.qa.rococo.grpc.RococoCountryServiceGrpc;
import guru.qa.rococo.grpc.RococoMuseumServiceGrpc;
import guru.qa.rococo.model.CountryJson;
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
public class GrpcCountryClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcCountryClient.class);
    private static final Empty EMPTY = Empty.getDefaultInstance();

    @GrpcClient("grpcCustomClient")
    private RococoCountryServiceGrpc.RococoCountryServiceBlockingStub rococoCountryServiceStub;

    public @Nonnull
    Page<CountryJson> getAllCountry(Pageable pageable) {
        try {
            // gRPC запрос с пагинацией
            PageableRequest request = PageableRequest.newBuilder()
                    .setPage(pageable.getPageNumber())
                    .setSize(pageable.getPageSize())
                    .build();

            // выполняем gRPC запрос
            CountriesResponse response = rococoCountryServiceStub.getAllCountries(request);

            // преобразуем ответ в Page<MuseumJson>
            List<CountryJson> museums = response.getAllCountriesList().stream()
                    .map(CountryJson::fromGrpcMessage)
                    .toList();

            return new PageImpl<>(museums, pageable, response.getTotalElements());

        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }
}
