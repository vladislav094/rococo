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

    @User
    @ApiLogin
    @Test
    @DisplayName("Добавляем новый музей")
    void testAddingNewMuseum() {

        final String randomMuseumTitle = RandomDataUtils.randomMuseumTitle();
        final String successfulUpdateMessage = String.format("Добавлен музей: %s", randomMuseumTitle);
        final String museumPhotoPath = "img/museum.jpeg";

        Selenide.open(MuseumPage.URL, MuseumPage.class)
                .clickAddMuseumButton()
                .setTitle(randomMuseumTitle)
                .setCity(randomCity())
                .selectCountry(randomCountry())
                .setDescription(randomDescription())
                .uploadPhoto(museumPhotoPath)
                .clickSubmitButton();
        page.museumPage.checkAlertMessage(successfulUpdateMessage);
    }

    @Museum
    @Test
    @DisplayName("Редактируем описание и фото музея")
    void testMuseumShouldChangedAfterEdit(MuseumJson museumJson) {

    }

}
