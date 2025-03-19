package guru.qa.rococo.data.projection;

import java.util.UUID;

public record UserWithStatus(
    UUID id,
    String username,
    String firstname,
    String lastname,
    byte[] avatar
) {
}
