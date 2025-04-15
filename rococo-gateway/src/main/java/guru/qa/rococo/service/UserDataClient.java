package guru.qa.rococo.service;

import guru.qa.rococo.model.UserJson;
import jakarta.annotation.Nonnull;

public interface UserDataClient {

    @Nonnull
    UserJson currentUser(@Nonnull String username);
}
