package guru.qa.rococo.service.api;

import com.google.protobuf.ByteString;
import guru.qa.rococo.grpc.GetCurrentUserRequest;
import guru.qa.rococo.grpc.RococoUserdataServiceGrpc;
import guru.qa.rococo.grpc.UserdataGrpc;
import guru.qa.rococo.model.UserJson;
import guru.qa.rococo.service.UserDataClient;
import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class GrpcUserClient implements UserDataClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcUserClient.class);

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

    public @Nonnull UserJson updateUserInfo(@Nonnull UserJson userJson) {
        try {
            UserdataGrpc request = UserdataGrpc.newBuilder()
                    .setId(userJson.id().toString())
                    .setUsername(userJson.username())
                    .setFirstname(userJson.firstname())
                    .setLastname(userJson.lastname())
                    .setAvatar(ByteString.copyFromUtf8(userJson.avatar()))
                    .build();
            return UserJson.fromGrpcMessage(rococoUserdataServiceStub.updateUser(request));
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }
}
