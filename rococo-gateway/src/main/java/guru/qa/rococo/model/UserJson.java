package guru.qa.rococo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.protobuf.ByteString;
import guru.qa.rococo.config.RococoGatewayServiceConfig;
import guru.qa.rococo.grpc.UserdataGrpc;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserJson(
        @JsonProperty("id")
        UUID id,
        @JsonProperty("username")
        String username,
        @JsonProperty("firstname")
        @Size(max = 30, message = "First name can`t be longer than 30 characters")
        String firstname,
        @JsonProperty("lastname")
        @Size(max = 50, message = "Lastname can`t be longer than 50 characters")
        String lastname,
        @JsonProperty("avatar")
        @Size(max = RococoGatewayServiceConfig.ONE_MB)
        String avatar) {

    public @Nonnull UserJson addUsername(@Nonnull String username) {
        return new UserJson(id, username, firstname, lastname, avatar);
    }

    public static @Nonnull UserJson fromGrpcMessage(@Nonnull UserdataGrpc user) {
        return new UserJson(
                UUID.fromString(user.getId()),
                user.getUsername(),
                user.getFirstname(),
                user.getLastname(),
                convertPhotoFromBase64(user.getAvatar())
        );
    }

    private static String convertPhotoFromBase64(ByteString photo) {
        if (photo == null || photo.isEmpty()) {
            return "";
        }
        return photo.toStringUtf8();
    }
}
