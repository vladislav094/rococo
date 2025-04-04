package guru.qa.rococo.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.annotation.meta.WebTest;
import guru.qa.rococo.model.rest.UserJson;
import guru.qa.rococo.page.RegisterPage;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.utils.RandomDataUtils.randomPassword;
import static guru.qa.rococo.utils.RandomDataUtils.randomUsername;

@WebTest
@DisplayName("Страница регистрации")
public class RegistrationWebTest extends BaseWebTest {

    private final String username = randomUsername();
    private final String validPassword = randomPassword(4, 10);

    @Test
    @Story("Успешная регистрация нового пользователя")
    @DisplayName("Выполняем регистрацию нового пользователя и проверяем текст об успешной регистрации")
    void testShouldRegisterNewUser() {

        final String successfulRegistrationText = "Добро пожаловать в Rococo";

        Selenide.open(RegisterPage.URL, RegisterPage.class)
                .setUsername(username)
                .setPassword(validPassword)
                .setConfirmPassword(validPassword)
                .clickSignUpButton()
                .checkMessageThatRegistrationWasSuccessful(successfulRegistrationText);
    }

    @User
    @Test
    @Story("Неуспешная регистрация")
    @DisplayName("Попытка регистрации с username ранее зарегистрированного пользователя")
    void testShouldNotRegisterUserWithExistingUsername(UserJson user) {

        final String earlieRegisterUserName = user.username();

        Selenide.open(RegisterPage.URL, RegisterPage.class)
                .setUsername(earlieRegisterUserName)
                .setPassword(validPassword)
                .setConfirmPassword(validPassword)
                .clickSignUpButton()
                .checkMessageThatUsernameAlreadyExist(earlieRegisterUserName);
    }

    @Test
    @Story("Неуспешная регистрация")
    @DisplayName("Попытка регистрации с разными значениями для поля password и passwordSubmit")
    void testShouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {

        final String passwordsNotEqualsMessage = "Passwords should be equal";

        Selenide.open(RegisterPage.URL, RegisterPage.class)
                .setUsername(username)
                .setPassword(validPassword)
                .setConfirmPassword(randomPassword(4, 10))
                .clickSignUpButton()
                .checkMessageThatPasswordAndSubmitPasswordNotEquals(passwordsNotEqualsMessage);
    }
}
