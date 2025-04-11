package guru.qa.rococo.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.annotation.meta.WebTest;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.page.MuseumPage;
import guru.qa.rococo.utils.RandomDataUtils;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.utils.RandomDataUtils.*;

@WebTest
@Story("Управлением разделом Музеи")
public class MuseumWebTest extends BaseWebTest {

    private final String museumPhotoPath = "img/museum.jpeg";

    @User
    @ApiLogin
    @Test
    @DisplayName("Создание нового музея")
    void testAddingNewMuseum() {

        final String randomMuseumTitle = RandomDataUtils.randomMuseumTitle();
        final String successfulCreateMessage = String.format("Добавлен музей: %s", randomMuseumTitle);

        Selenide.open(MuseumPage.URL, MuseumPage.class)
                .checkThatPageLoaded()
                .clickAddMuseumButton()
                .checkThatModalLoaded()
                .setTitle(randomMuseumTitle)
                .setCity(randomCity())
                .selectCountry(randomCountry())
                .setDescription(randomDescription())
                .uploadPhoto(museumPhotoPath)
                .clickSubmitButton();
        page.museumPage.checkAlertMessage(successfulCreateMessage);
    }

    @Museum
    @User
    @ApiLogin
    @Test
    @DisplayName("Редактирование музея")
    void testMuseumShouldChangedAfterEdit(MuseumJson museumJson) {

        final String currentMuseumPage = String.format("%s/%s", MuseumPage.URL, museumJson.id());
        final String randomMuseumTitle = RandomDataUtils.randomMuseumTitle();
        final String successfulUpdateMessage = String.format("Обновлен музей: %s", randomMuseumTitle);

        Selenide.open(currentMuseumPage, MuseumPage.class)
                .checkMuseumCardData(museumJson)
                .clickEditMuseumButton()
                .checkThatEditModalLoaded()
                .setTitle(randomMuseumTitle)
                .setCity(randomCity())
                .selectCountry(randomCountry())
                .setDescription(randomDescription())
                .uploadPhoto(museumPhotoPath)
                .clickSubmitButton();
        page.museumPage.checkAlertMessage(successfulUpdateMessage);
    }

    @Museum
    @User
    @ApiLogin
    @Test
    @DisplayName("Поиск музея по названию в строке поиска")
    void testMuseumShouldFindingByTitleInSearchField(MuseumJson museumJson) {

        final String currentMuseumTitle = museumJson.title();

        Selenide.open(MuseumPage.URL, MuseumPage.class)
                .checkThatPageLoaded()
                .fillSearchInput(currentMuseumTitle)
                .toMuseumCardByTitle(currentMuseumTitle)
                .checkMuseumCardData(museumJson);
    }
}
