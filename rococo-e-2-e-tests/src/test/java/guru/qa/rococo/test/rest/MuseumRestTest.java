package guru.qa.rococo.test.rest;

import guru.qa.rococo.jupiter.annotation.*;
import guru.qa.rococo.jupiter.annotation.meta.RestTest;
import guru.qa.rococo.jupiter.extension.ApiLoginExtension;
import guru.qa.rococo.model.rest.GeoJson;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.model.rest.pageable.RestResponsePage;
import guru.qa.rococo.service.impl.GatewayApiClient;
import guru.qa.rococo.utils.RandomDataUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import retrofit2.Response;

import static guru.qa.rococo.utils.ImageUtils.imageToStringBytes;

@RestTest
public class MuseumRestTest {

    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

    @Museum
    @Test
    @DisplayName("Получение музея по ID")
    void museumShouldReturned(MuseumJson museumJson) {

        SoftAssertions softly = new SoftAssertions();
        final Response<MuseumJson> response = gatewayApiClient.getMuseumById(museumJson.id().toString());
        final MuseumJson responseBody = response.body();

        softly.assertThat(response.code())
                .as("Код ответа")
                .isEqualTo(200);

        softly.assertThat(responseBody)
                .as("Тело ответа не должно быть null")
                .isNotNull();

        if (responseBody != null) {
            softly.assertThat(responseBody.id())
                    .as("ID музея должен совпадать")
                    .isEqualTo(museumJson.id());

            softly.assertThat(responseBody.title())
                    .as("Название музея должно совпадать")
                    .isEqualTo(museumJson.title());

            softly.assertThat(responseBody.description())
                    .as("Описание музея должно совпадать")
                    .isEqualTo(museumJson.description());

            softly.assertThat(responseBody.photo())
                    .as("Фото музея должно совпадать")
                    .isEqualTo(museumJson.photo());
        }

        softly.assertAll();
    }

    @Museum
    @Test
    @DisplayName("Получение списка музеев")
    void getAllMuseumsShouldReturnPaginatedResults() {

        SoftAssertions softly = new SoftAssertions();
        final Response<RestResponsePage<MuseumJson>> response = gatewayApiClient.getAllMuseums(0, 10, null);

        softly.assertThat(response.code())
                .as("Код ответа")
                .isEqualTo(200);

        softly.assertThat(response.body())
                .as("Тело ответа не должно быть null")
                .isNotNull();

        softly.assertThat(response.body().getContent())
                .as("Список музеев не должен быть пуст")
                .isNotEmpty();

        softly.assertAll();
    }

    @Museum
    @Test
    @DisplayName("Поиск музеев по названию")
    void shouldReturnFilteredResults(MuseumJson museumJson) {

        SoftAssertions softly = new SoftAssertions();
        final String museumTitle = museumJson.title();

        final Response<RestResponsePage<MuseumJson>> response = gatewayApiClient.getAllMuseums(0, 10, museumTitle);

        softly.assertThat(response.code())
                .as("Проверка кода ответа")
                .isEqualTo(200);

        softly.assertThat(response.body())
                .as("Тело ответа не должно быть null")
                .isNotNull();

        softly.assertThat(response.body().getContent())
                .as("В названии каждого музея должна быть подстрока из фильтра запроса")
                .allSatisfy(m ->
                        softly.assertThat(m.title())
                                .as("Проверка названия музея")
                                .containsIgnoringCase(museumTitle)
                );

        softly.assertAll();
    }

    @User
    @Geo
    @ApiLogin
    @Test
    @DisplayName("Создание нового музея")
    void museumShouldCreated(@Token String token, GeoJson geoJson) {

        SoftAssertions softly = new SoftAssertions();
        final MuseumJson museumJson = new MuseumJson(
                null,
                RandomDataUtils.randomMuseumTitle(),
                RandomDataUtils.randomDescription(),
                imageToStringBytes("img/museum.jpeg"),
                geoJson
        );

        final Response<MuseumJson> response = gatewayApiClient.addMuseum(token, museumJson);
        final MuseumJson createdMuseum = response.body();

        softly.assertThat(response.code())
                .as("Код ответа")
                .isEqualTo(200);

        softly.assertThat(createdMuseum)
                .as("Созданный музей не должен быть null")
                .isNotNull();

        if (createdMuseum != null) {
            softly.assertThat(createdMuseum.id())
                    .as("ID музея должно совпадать")
                    .isNotNull();

            softly.assertThat(createdMuseum.title())
                    .as("Название музея должно совпадать")
                    .isEqualTo(museumJson.title());

            softly.assertThat(createdMuseum.geo().city())
                    .as("Город музея должен совпадать")
                    .isEqualTo(museumJson.geo().city());
        }

        softly.assertAll();
    }

    @Museum
    @User
    @ApiLogin
    @Test
    @DisplayName("Обновление данных музея")
    void museumShouldUpdated(@Token String token, MuseumJson museumJson) {

        SoftAssertions softly = new SoftAssertions();
        final MuseumJson museumFromAnno = gatewayApiClient.getMuseumById(museumJson.id().toString()).body();
        final MuseumJson updatedMuseum = new MuseumJson(
                museumFromAnno.id(),
                RandomDataUtils.randomMuseumTitle(),
                RandomDataUtils.randomDescription(),
                imageToStringBytes("img/picasso.jpeg"),
                museumFromAnno.geo()
        );

        final Response<MuseumJson> response = gatewayApiClient.updateMuseum(token, updatedMuseum);
        final MuseumJson museumResponseBody = response.body();

        softly.assertThat(response.code())
                .as("Проверка кода ответа")
                .isEqualTo(200);

        softly.assertThat(museumResponseBody)
                .as("Обновленный музей не должен быть null")
                .isNotNull();


        if (museumResponseBody != null) {
            softly.assertThat(museumResponseBody.title())
                    .as("Название музея")
                    .isEqualTo(updatedMuseum.title());

            softly.assertThat(museumResponseBody.title())
                    .as("Название музея должно совпадать")
                    .isEqualTo(updatedMuseum.title());

            softly.assertThat(museumResponseBody.geo().city())
                    .as("Город музея должен совпадать")
                    .isEqualTo(updatedMuseum.geo().city());

        }
        softly.assertAll();
    }
}
