package guru.qa.rococo.test.rest;

import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.Token;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.annotation.meta.RestTest;
import guru.qa.rococo.jupiter.extension.ApiLoginExtension;
import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.model.rest.pageable.RestResponsePage;
import guru.qa.rococo.service.impl.GatewayApiClient;
import guru.qa.rococo.utils.RandomDataUtils;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import retrofit2.Response;

import java.util.UUID;

import static guru.qa.rococo.utils.ImageUtils.imageToStringBytes;

@RestTest
public class ArtistRestTest {

    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

    @Artist
    @Test
    @DisplayName("Получение художника по ID")
    void artistShouldReturned(ArtistJson artist) {

        SoftAssertions softly = new SoftAssertions();
        final Response<ArtistJson> response = gatewayApiClient.getArtistById(artist.id().toString());
        final ArtistJson responseBody = response.body();

        softly.assertThat(response.code())
                .as("Проверка кода ответа")
                .isEqualTo(200);

        softly.assertThat(responseBody)
                .as("Тело ответа не должно быть null")
                .isNotNull();

        if (responseBody != null) {
            softly.assertThat(responseBody.id())
                    .as("ID художника должен совпадать")
                    .isEqualTo(artist.id());

            softly.assertThat(responseBody.name())
                    .as("Имя художника должно совпадать")
                    .isEqualTo(artist.name());

            softly.assertThat(responseBody.biography())
                    .as("Биография художника должна совпадать")
                    .isEqualTo(artist.biography());

            softly.assertThat(responseBody.photo())
                    .as("Фото художника должно совпадать")
                    .isEqualTo(artist.photo());
        }
        softly.assertAll();
    }

    @User
    @ApiLogin
    @Test
    @DisplayName("Создание нового художника")
    void artistShouldCreated(@Token String token) {

        SoftAssertions softly = new SoftAssertions();
        final String artistPhoto = imageToStringBytes("img/picasso.jpeg");
        final ArtistJson artist = new ArtistJson(
                null,
                RandomDataUtils.randomArtistName(),
                RandomDataUtils.randomDescription(),
                artistPhoto
        );

        Response<ArtistJson> response = gatewayApiClient.addArtist(token, artist);
        ArtistJson responseBody = response.body();

        softly.assertThat(response.code())
                .as("Проверка кода ответа")
                .isEqualTo(200);

        softly.assertThat(responseBody)
                .as("Тело ответа")
                .isNotNull();

        if (responseBody != null) {
            softly.assertThat(responseBody.id())
                    .as("ID художника")
                    .isNotNull();

            softly.assertThat(responseBody.name())
                    .as("Имя художника")
                    .isEqualTo(artist.name());

            softly.assertThat(responseBody.biography())
                    .as("Биография")
                    .isEqualTo(artist.biography());

            softly.assertThat(responseBody.photo())
                    .as("Фото")
                    .isEqualTo(artist.photo());
        }

        softly.assertAll();
    }

    @Artist
    @Test
    @DisplayName("Получение списка художников")
    void getAllArtistsShouldReturnPaginatedResults() {

        SoftAssertions softly = new SoftAssertions();
        final Response<RestResponsePage<ArtistJson>> response = gatewayApiClient.getAllArtists(0, 10, null);

        softly.assertThat(response.code())
                .as("Проверка кода ответа")
                .isEqualTo(200);

        softly.assertThat(response.body())
                .as("Тело ответа не должно быть null")
                .isNotNull();

        softly.assertThat(response.body().getContent())
                .as("Список художников не должен быть пустым")
                .isNotEmpty();

        softly.assertAll();
    }

    @Artist
    @Test
    @DisplayName("Поиск художников по имени")
    void shouldReturnFilteredResults(ArtistJson artist) {

        SoftAssertions softly = new SoftAssertions();
        final String artistName = artist.name();

        final Response<RestResponsePage<ArtistJson>> response = gatewayApiClient.getAllArtists(0, 10, artistName);

        softly.assertThat(response.code())
                .as("Проверка кода ответа")
                .isEqualTo(200);

        softly.assertThat(response.body())
                .as("Тело ответа не должно быть null")
                .isNotNull();

        softly.assertThat(response.body().getContent())
                .as("В имени каждого художника должна быть подстрока из фильтра запроса")
                .allSatisfy(artistJson ->
                        softly.assertThat(artistJson.name())
                                .as("Проверка имени художника")
                                .containsIgnoringCase(artistName)
                );

        softly.assertAll();
    }

    @User
    @Artist
    @ApiLogin
    @Test
    @DisplayName("Обновление данных художника")
    void artistShouldUpdated(@Token String token, ArtistJson artistJson) {

        SoftAssertions softly = new SoftAssertions();
        final ArtistJson artistFromAnno = gatewayApiClient.getArtistById(artistJson.id().toString()).body();
        final ArtistJson updatedArtist = new ArtistJson(
                artistFromAnno.id(),
                RandomDataUtils.randomArtistName(),
                RandomDataUtils.randomDescription(),
                imageToStringBytes("img/malevich.jpg")
        );

        final Response<ArtistJson> response = gatewayApiClient.updateArtist(token, updatedArtist);
        final ArtistJson responseArtist = response.body();

        softly.assertThat(response.code())
                .as("Проверка кода ответа")
                .isEqualTo(200);

        softly.assertThat(responseArtist)
                .as("Проверка тела ответа")
                .isNotNull();

        if (responseArtist != null) {
            softly.assertThat(responseArtist.id())
                    .as("ID художника не должен измениться")
                    .isEqualTo(artistFromAnno.id());

            softly.assertThat(responseArtist.name())
                    .as("Имя художника должно обновиться")
                    .isEqualTo(updatedArtist.name());

            softly.assertThat(responseArtist.biography())
                    .as("Биография должна обновиться")
                    .isEqualTo(updatedArtist.biography());

            softly.assertThat(responseArtist.photo())
                    .as("Фото должно обновиться")
                    .isEqualTo(updatedArtist.photo());
        }

        softly.assertAll();
    }

    //TODO  необходимо доработать исключение на уровне сервиса
    @User
    @ApiLogin
    @Test
    @DisplayName("Ошибка при обновлении несуществующего художника")
    void errorShouldReturnedWhenUpdateNotExistingArtist(@Token String token) {

        SoftAssertions softly = new SoftAssertions();
        final ArtistJson nonExistingArtist = new ArtistJson(
                UUID.randomUUID(),
                "Name",
                "Biography",
                "photo"
        );

        Response<ArtistJson> response = gatewayApiClient.updateArtist(token, nonExistingArtist);

        softly.assertThat(response.code())
                .as("Проверка кода ответа")
                .isEqualTo(404);

        softly.assertThat(response.body())
                .as("Тело ответа должно быть null")
                .isNull();

        softly.assertAll();
    }

    @Test
    @DisplayName("Ошибка авторизации при обновлении художника с невалидным токеном")
    void unauthorizedErrorShouldReturnedWhenUpdateArtistWithInvalidAuthToken() {

        SoftAssertions softly = new SoftAssertions();
        ArtistJson artist = new ArtistJson(
                UUID.randomUUID(),
                "Name",
                "Biography",
                "photo"
        );

        Response<ArtistJson> response = gatewayApiClient.updateArtist("", artist);

        softly.assertThat(response.code())
                .as("Проверка кода ответа")
                .isEqualTo(401);

        softly.assertThat(response.body())
                .as("Тело ответа должно быть null")
                .isNull();

        softly.assertAll();
    }
}
