package guru.qa.rococo.service;

import guru.qa.rococo.ex.CountryNotFoundException;
import guru.qa.rococo.ex.MuseumNotFoundException;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpcExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcExceptionHandler.class);

    public static void handleException(StreamObserver<?> responseObserver, Exception e) {
        if (e instanceof IllegalArgumentException) {
            LOG.error("Invalid argument: {}", e.getMessage(), e);
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } else if (e instanceof MuseumNotFoundException || e instanceof CountryNotFoundException) {
            LOG.error("Resource not found: {}", e.getMessage(), e);
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription(e.getMessage())
                    .asRuntimeException());
        } else {
            LOG.error("Internal server error: {}", e.getMessage(), e);
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error: " + e.getMessage())
                    .asRuntimeException());
        }
    }
}
