package guru.qa.rococo.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.page.ArtistPage;
import guru.qa.rococo.utils.RandomDataUtils;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.utils.RandomDataUtils.randomDescription;

@Story("Управлением разделом Художники")
public class ArtistWebTest extends BaseWebTest {

    @User
    @ApiLogin
    @Test
    @DisplayName("Создание нового художника")
    void testAddingNewArtist() {

        final String artistPhotoPath = "img/picasso.jpeg";
        final String randomArtistName = RandomDataUtils.randomArtistName();
        final String successfulCreateMessage = String.format("Добавлен художник: %s", randomArtistName);

        Selenide.open(ArtistPage.URL, ArtistPage.class)
                .checkThatPageLoaded()
                .clickAddArtistButton()
                .checkThatModalLoaded()
                .setName(randomArtistName)
                .uploadPhoto(artistPhotoPath)
                .setBiography(RandomDataUtils.randomDescription())
                .clickSubmitButton();
        page.artistPage.checkAlertMessage(successfulCreateMessage);
    }

    @Artist
    @User
    @ApiLogin
    @Test
    @DisplayName("Редактирование художника")
    void testArtistShouldChangedAfterEdit(ArtistJson artistJson) {

        final String newArtistPhotoPath = "img/malevich.jpg";

        final String currentArtistPage = String.format("%s/%s", ArtistPage.URL, artistJson.id());
        final String randomArtistName = RandomDataUtils.randomArtistName();
        final String successfulUpdateMessage = String.format("Обновлен художник: %s", randomArtistName);

        Selenide.open(currentArtistPage, ArtistPage.class)
                .checkArtistCardData(artistJson, null)
                .clickEditArtistButton()
                .checkThatEditModalLoaded()
                .setName(randomArtistName)
                .setBiography(randomDescription())
                .uploadPhoto(newArtistPhotoPath)
                .clickSubmitButton();
        page.artistPage.checkAlertMessage(successfulUpdateMessage);
    }

    @Artist
    @User
    @ApiLogin
    @Test
    @DisplayName("Поиск художника по названию в строке поиска")
    void testArtistShouldFindingByNameInSearchField(ArtistJson artistJson) {

        final String currentArtistName = artistJson.name();

        Selenide.open(ArtistPage.URL, ArtistPage.class)
                .checkThatPageLoaded()
                .fillSearchInput(currentArtistName)
                .toArtistCardByName(currentArtistName)
                .checkArtistCardData(artistJson, null);
    }
}
