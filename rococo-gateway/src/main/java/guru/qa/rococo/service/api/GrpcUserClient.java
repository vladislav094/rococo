package guru.qa.rococo.service.api;

import com.google.protobuf.Empty;
import guru.qa.rococo.grpc.GetCurrentUserRequest;
import guru.qa.rococo.grpc.RococoUserdataServiceGrpc;
import guru.qa.rococo.model.UserJson;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GrpcUserClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcUserClient.class);
    private static final Empty EMPTY = Empty.getDefaultInstance();

    @GrpcClient("userdataGrpcClient")
    private RococoUserdataServiceGrpc.RococoUserdataServiceBlockingStub rococoUserdataServiceStub;

    public @Nonnull UserJson currentUser(@Nonnull String username) {
        try {
            GetCurrentUserRequest request = GetCurrentUserRequest.newBuilder()
                    .setUsername(username)
                    .build();
            return UserJson.fromGrpcMessage(rococoUserdataServiceStub.getCurrentUser(request));
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }
}
