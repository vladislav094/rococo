package guru.qa.rococo.service;

import guru.qa.rococo.model.rest.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface UserdataClient {

    @Nonnull
    UserJson createUser(String username, String password);

    @Nullable
    public UserJson getUserByName(@Nonnull String name);
}
