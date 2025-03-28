package guru.qa.rococo.clients;

import com.google.protobuf.Empty;
import guru.qa.rococo.grpc.*;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class GrpcCountryClient {

    private final RococoCountryServiceGrpc.RococoCountryServiceFutureStub countryFutureStub;

    @Autowired
    public GrpcCountryClient(@Qualifier("countryChannel") Channel channel) {
        this.countryFutureStub = RococoCountryServiceGrpc.newFutureStub(channel);
    }


    public CompletableFuture<CountryResponse> getCountryAsync(String countryId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
//                return countryFutureStub.getMuseumById(ByIdRequest.newBuilder().setId(museumId).build()).get();
                return null;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
