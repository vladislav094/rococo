package guru.qa.rococo.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.ScreenShotTest;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.annotation.meta.WebTest;
import guru.qa.rococo.page.MainPage;
import guru.qa.rococo.utils.RandomDataUtils;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;

@WebTest
@Story("Управление профилем пользователя")
public class ProfileWebTest extends BaseWebTest {

    @User
    @ApiLogin
    @Test
    @DisplayName("Обновляем имя и фамилию пользователя в профиле")
    void testUserDataShouldChangedAfterEditInProfile() {

        final String successfulUpdateMessage = "Профиль обновлен";

        String randomFirstname = RandomDataUtils.randomUsername().split("\\.")[0];
        String randomLastname = RandomDataUtils.randomUsername().split("\\.")[1];

        Selenide.open(MainPage.URL, MainPage.class)
                .checkThatPageLoaded()
                .getProfileModal()
                .checkThatModalLoaded()
                .setFirstname(randomFirstname)
                .setLastname(randomLastname)
                .clickSubmitButton();
        new MainPage().checkAlertMessage(successfulUpdateMessage);
    }

    @User
    @ApiLogin
    @ScreenShotTest(value = "img/expected-avatar-screen.png")
    @Test
    @DisplayName("Обновляем аватар профиля")
    void testAvatarAfterUploading(BufferedImage expected) {

        Selenide.open(MainPage.URL, MainPage.class)
                .checkThatPageLoaded()
                .getProfileModal()
                .checkThatModalLoaded()
                .uploadPicture("img/avatar-for-upload.png")
                .clickSubmitButton();
        new MainPage().checkThatAvatarEqualsUploadingImage(expected);
    }

    @User
    @ApiLogin
    @Test
    @DisplayName("Выполняем выход из сессии пользователя")
    void testUserSessionShouldStoppedAfterLogout() {

        final String sessionStoppedMessage = "Сессия завершена";

        Selenide.open(MainPage.URL, MainPage.class)
                .checkThatPageLoaded()
                .getProfileModal()
                .checkThatModalLoaded()
                .logout()
                .checkAlertMessage(sessionStoppedMessage);
    }
}
