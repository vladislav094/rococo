package guru.qa.rococo.clients;

import guru.qa.rococo.grpc.ArtistResponse;
import guru.qa.rococo.grpc.ByIdRequest;
import guru.qa.rococo.grpc.ByNameRequest;
import guru.qa.rococo.grpc.RococoArtistServiceGrpc;
import io.grpc.Channel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class GrpcArtistClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcArtistClient.class);

    private final RococoArtistServiceGrpc.RococoArtistServiceFutureStub artistFutureStub;
    private final RetryTemplate retryTemplate;

    @Autowired
    public GrpcArtistClient(@Qualifier("artistChannel") Channel channel, RetryTemplate retryTemplate) {
        this.artistFutureStub = RococoArtistServiceGrpc.newFutureStub(channel);
        this.retryTemplate = retryTemplate;
    }

    public CompletableFuture<ArtistResponse> getArtistAsync(String artistId) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return artistFutureStub.getArtistById(ByIdRequest.newBuilder().setId(artistId).build()).get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public CompletableFuture<UUID> getArtistIdByNameAsync(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ByNameRequest request = ByNameRequest.newBuilder()
                        .setName(name)
                        .build();

                ArtistResponse response = artistFutureStub.artistByName(request)
                        .get(5, TimeUnit.SECONDS);

                return UUID.fromString(response.getId());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
