package guru.qa.rococo.api;

import guru.qa.rococo.model.rest.UserJson;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface GatewayApi {

    @GET("/api/user")
    Call<UserJson> getUser(@Header("Authorization") String bearerToken);
}

