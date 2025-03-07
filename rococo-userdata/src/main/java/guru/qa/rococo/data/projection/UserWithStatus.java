package guru.qa.rococo.data.projection;

import guru.qa.rococo.data.CurrencyValues;
import guru.qa.rococo.data.FriendshipStatus;

import java.util.UUID;

public record UserWithStatus(
    UUID id,
    String username,
    CurrencyValues currency,
    String fullname,
    byte[] photoSmall,
    FriendshipStatus status
) {
}
