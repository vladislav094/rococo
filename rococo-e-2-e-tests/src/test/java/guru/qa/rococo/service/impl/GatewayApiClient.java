package guru.qa.rococo.service.impl;

import guru.qa.rococo.api.GatewayApi;
import guru.qa.rococo.api.core.RestClient;
import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.model.rest.pageable.RestResponsePage;
import io.qameta.allure.Step;
import retrofit2.Call;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;

public class GatewayApiClient extends RestClient {

    private final GatewayApi gatewayApi;

    public GatewayApiClient() {
        super(CFG.gatewayUrl());
        this.gatewayApi = create(GatewayApi.class);
    }

    @Step("Send GET /api/artist request to rococo-gateway")
    public Response<RestResponsePage<ArtistJson>> getAllArtists(int page, int size, @Nullable String name) {
        return executeRequest(gatewayApi.getAllArtists(page, size, name));
    }

    @Step("Send GET /api/artist/{id} request to rococo-gateway")
    public Response<ArtistJson> getArtistById(@Nonnull String id) {
        return executeRequest(gatewayApi.getArtistById(id));
    }

    @Step("Send POST /api/artist request to rococo-gateway")
    public Response<ArtistJson> addArtist(@Nonnull String bearerToken, @Nonnull ArtistJson artist) {
        return executeRequest(gatewayApi.addArtist(bearerToken, artist));
    }

    @Step("Send PATCH /api/artist request to rococo-gateway")
    public Response<ArtistJson> updateArtist(@Nonnull String bearerToken, @Nonnull ArtistJson artist) {
        return executeRequest(gatewayApi.updateArtist(bearerToken, artist));
    }

    @Step("Send GET /api/museum/{id} request")
    public Response<MuseumJson> getMuseumById(@Nonnull String id) {
        return executeRequest(gatewayApi.getMuseumById(id));
    }

    @Step("Send GET /api/museum request")
    public Response<RestResponsePage<MuseumJson>> getAllMuseums(int page, int size, @Nullable String title) {
        return executeRequest(gatewayApi.getAllMuseums(page, size, title));
    }

    @Step("Send POST /api/museum request")
    public Response<MuseumJson> addMuseum(@Nonnull String bearerToken, @Nonnull MuseumJson museum) {
        return executeRequest(gatewayApi.addMuseum(bearerToken, museum));
    }

    @Step("Send PATCH /api/museum request")
    public Response<MuseumJson> updateMuseum(@Nonnull String bearerToken, @Nonnull MuseumJson museum) {
        return executeRequest(gatewayApi.updateMuseum(bearerToken, museum));
    }

    private <T> Response<T> executeRequest(Call<T> call) {
        try {
            return call.execute();
        } catch (IOException e) {
            throw new RuntimeException("HTTP request failed: " + e.getMessage(), e);
        }
    }
}
