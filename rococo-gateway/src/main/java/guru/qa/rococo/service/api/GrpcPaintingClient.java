package guru.qa.rococo.service.api;

import com.google.protobuf.Empty;
import guru.qa.rococo.grpc.RococoMuseumServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class GrpcPaintingClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcPaintingClient.class);
    private static final Empty EMPTY = Empty.getDefaultInstance();

    @GrpcClient("paintingGrpcClient")
    private RococoMuseumServiceGrpc.RococoMuseumServiceBlockingStub rococoMuseumServiceStub;
}
