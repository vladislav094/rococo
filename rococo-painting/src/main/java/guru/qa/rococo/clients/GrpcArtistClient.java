package guru.qa.rococo.clients;

import guru.qa.rococo.grpc.*;
import io.grpc.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class GrpcArtistClient {

    private final RococoArtistServiceGrpc.RococoArtistServiceFutureStub artistFutureStub;

    @Autowired
    public GrpcArtistClient(@Qualifier("artistChannel") Channel channel) {
        this.artistFutureStub = RococoArtistServiceGrpc.newFutureStub(channel);
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
}
