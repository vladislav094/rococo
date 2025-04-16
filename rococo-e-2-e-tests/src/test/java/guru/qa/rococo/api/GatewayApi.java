package guru.qa.rococo.api;

import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.model.rest.UserJson;
import guru.qa.rococo.model.rest.pageable.RestResponsePage;
import retrofit2.Call;
import retrofit2.http.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface GatewayApi {

    @GET("api/user")
    Call<UserJson> getUser(@Header("Authorization") String bearerToken);

    @GET("api/artist/{id}")
    Call<ArtistJson> getArtistById(@Path("id") @Nonnull String id);

    @GET("api/artist")
    Call<RestResponsePage<ArtistJson>> getAllArtists(@Query("page") int page,
                                                     @Query("size") int size,
                                                     @Query("name") @Nullable String name);

    @POST("api/artist")
    Call<ArtistJson> addArtist(@Header("Authorization") String bearerToken,
                               @Body ArtistJson artist);

    @PATCH("api/artist")
    Call<ArtistJson> updateArtist(@Header("Authorization") String bearerToken,
                                  @Body ArtistJson artist);

    @GET("api/museum/{id}")
    Call<MuseumJson> getMuseumById(@Path("id") @Nonnull String id);

    @GET("api/museum")
    Call<RestResponsePage<MuseumJson>> getAllMuseums(@Query("page") int page,
                                                     @Query("size") int size,
                                                     @Query("title") @Nullable String title);

    @POST("api/museum")
    Call<MuseumJson> addMuseum(@Header("Authorization") String bearerToken,
                               @Body MuseumJson museum);

    @PATCH("api/museum")
    Call<MuseumJson> updateMuseum(@Header("Authorization") String bearerToken,
                                  @Body MuseumJson museum);
}

