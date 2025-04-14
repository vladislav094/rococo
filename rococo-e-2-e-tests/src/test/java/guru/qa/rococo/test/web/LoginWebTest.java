package guru.qa.rococo.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.annotation.meta.WebTest;
import guru.qa.rococo.model.rest.UserJson;
import guru.qa.rococo.page.MainPage;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.utils.RandomDataUtils.randomPassword;

@Story("Страница авторизации")
public class LoginWebTest extends BaseWebTest {

    @User
    @Test
    @DisplayName("Выполняем авторизацию пользователя")
    void testMainPageShouldBeDisplayedAfterSuccessfulLogin(UserJson user) {

        Selenide.open(MainPage.URL, MainPage.class)
                .checkThatPageLoaded()
                .getHeader()
                .toLoginPage()
                .login(user.username(), user.testData().password())
                .checkSuccessfulLogin();
    }

    @User
    @Test
    @DisplayName("Выполняем авторизацию пользователя с невалидным паролем")
    void testUserShouldStayOnLoginPageAfterLoginWithBadCredential(UserJson user) {

        final String badCredentialsMessage = "Неверные учетные данные пользователя";

        Selenide.open(MainPage.URL, MainPage.class)
                .getHeader()
                .toLoginPage()
                .login(user.username(), randomPassword(1, 3));

        page.loginPage.checkMessageThatWasInputBadCredentials(badCredentialsMessage);
    }
}
