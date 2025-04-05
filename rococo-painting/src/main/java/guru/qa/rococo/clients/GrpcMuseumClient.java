package guru.qa.rococo.clients;

import guru.qa.rococo.grpc.ByIdRequest;
import guru.qa.rococo.grpc.ByTitleRequest;
import guru.qa.rococo.grpc.MuseumResponse;
import guru.qa.rococo.grpc.RococoMuseumServiceGrpc;
import io.grpc.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GrpcMuseumClient {

    private final RococoMuseumServiceGrpc.RococoMuseumServiceFutureStub museumFutureStub;
    private final RetryTemplate retryTemplate;

    @Autowired
    public GrpcMuseumClient(@Qualifier("museumChannel") Channel channel, RetryTemplate retryTemplate) {
        this.museumFutureStub = RococoMuseumServiceGrpc.newFutureStub(channel);
        this.retryTemplate = retryTemplate;
    }


    public CompletableFuture<MuseumResponse> getMuseumAsync(String museumId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return museumFutureStub.getMuseumById(ByIdRequest.newBuilder().setId(museumId).build()).get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<UUID> getMuseumIdByTitleAsync(String title) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ByTitleRequest request = ByTitleRequest.newBuilder()
                        .setTitle(title)
                        .build();

                MuseumResponse response = museumFutureStub.museumByTitle(request)
                        .get(5, TimeUnit.SECONDS);

                return UUID.fromString(response.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
