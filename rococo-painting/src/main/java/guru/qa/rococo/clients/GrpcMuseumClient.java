package guru.qa.rococo.clients;

import guru.qa.rococo.grpc.ByIdRequest;
import guru.qa.rococo.grpc.MuseumResponse;
import guru.qa.rococo.grpc.RococoMuseumServiceGrpc;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class GrpcMuseumClient {

    private final RococoMuseumServiceGrpc.RococoMuseumServiceFutureStub museumFutureStub;

    @Autowired
    public GrpcMuseumClient(@Qualifier("museumChannel") Channel channel) {
        this.museumFutureStub = RococoMuseumServiceGrpc.newFutureStub(channel);
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
}
