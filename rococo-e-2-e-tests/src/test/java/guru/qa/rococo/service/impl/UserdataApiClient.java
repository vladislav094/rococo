package guru.qa.rococo.service.impl;

import guru.qa.rococo.api.AuthApi;
import guru.qa.rococo.api.GatewayApi;
import guru.qa.rococo.api.core.RestClient;
import guru.qa.rococo.api.core.ThreadSafeCookieStore;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.model.rest.TestData;
import guru.qa.rococo.model.rest.UserJson;
import guru.qa.rococo.service.UserdataClieint;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class UserdataApiClient implements UserdataClieint {

    private static final Config CFG = Config.getInstance();
    private static final String defaultPassword = "12345";

    private final GatewayApi gatewayApi = new RestClient.EmptyClient("http://127.0.0.1:8090/").create(GatewayApi.class);
    private final AuthApi authApi = new RestClient.EmptyClient(CFG.authUrl()).create(AuthApi.class);

    private final AuthApiClient authApiClient = new AuthApiClient();
    private final UserdataClieint userdataClieint = new UserdataDbClient();

    @Override
    @Nonnull
    @Step("Create user using API")
    public UserJson createUser(String username, String password) {
        try {
            Response<Void> registerFrormResponse = authApi.getRegisterForm().execute();
            if(!registerFrormResponse.isSuccessful()) {
                throw new RuntimeException("Failed to get register form: " + registerFrormResponse.code());
            }
            String csrfToken = ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN");
            if (csrfToken == null) {
                throw new RuntimeException("CSRF token not found in cookies");
            }

            Response<Void> registerResponse = authApi.registerUser(
                    username,
                    password,
                    password,
                    csrfToken
            ).execute();

            if (!registerResponse.isSuccessful()) {
                String errorBody = registerResponse.errorBody() != null ?
                        registerResponse.errorBody().string() : "empty body";
                throw new RuntimeException("Registration failed: " + errorBody);
            }

            String token = authApiClient.login(username, password);
            if (token == null || token.isEmpty()) {
                throw new RuntimeException("Login failed: empty token");
            }


            Response<UserJson> userResponse = gatewayApi.getUser("Bearer " + token).execute();
            if (!userResponse.isSuccessful() || userResponse.body() == null) {
                throw new RuntimeException("Failed to get user data");
            }

            return userResponse.body().addTestData(new TestData(password));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    @Override
    public UserJson getUserByName(@NotNull String name) {
        return null;
    }
}