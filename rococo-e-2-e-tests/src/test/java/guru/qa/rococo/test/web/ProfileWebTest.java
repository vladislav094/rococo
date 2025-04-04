package guru.qa.rococo.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.ScreenShotTest;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.annotation.meta.WebTest;
import guru.qa.rococo.model.rest.UserJson;
import guru.qa.rococo.page.MainPage;
import guru.qa.rococo.utils.RandomDataUtils;
import io.qameta.allure.Story;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

@DisplayName("Страница профиля")
@WebTest
public class ProfileWebTest extends BaseWebTest {

    @User
    @ApiLogin
    @Test
    @Story("Успешное редактирование профиля")
    @DisplayName("Обновляем имя и фамилию пользователя в профиле")
    void test(UserJson user) {

        final String successfulUpdateMessage = "Профиль обновлен";

        String randomFirstname = RandomDataUtils.randomUsername().split("\\.")[0];
        String randomLastname = RandomDataUtils.randomUsername().split("\\.")[1];
        Selenide.open(MainPage.URL, MainPage.class)
                .getProfileModal()
                .setFirstname(randomFirstname)
                .setLastname(randomLastname)
                .updateProfile()
                .checkAlertMessage(successfulUpdateMessage);
    }

    @User
    @ApiLogin
    @ScreenShotTest(value = "img/expected-avatar-screen.png")
    @Test
    @Story("Успешное редактирование профиля")
    @DisplayName("Выполняем редактирование профиля")
    void checkAvatarAfterUploading(BufferedImage expected) {

        Selenide.open(MainPage.URL, MainPage.class)
                .getProfileModal()
                .uploadPicture("img/avatar-for-upload.png")
                .updateProfile()
                .checkThatAvatarEqualsUploadingImage(expected);
    }
}
