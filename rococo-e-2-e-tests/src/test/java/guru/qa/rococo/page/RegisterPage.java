package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class RegisterPage extends BasePage<RegisterPage> {

    public static final String URL = CFG.authUrl() + "register";

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement confirmPassword = $("input[name='passwordSubmit']");
    private final SelenideElement signUpButton = $("button[type=submit]");
    private final SelenideElement signInLink = $(".form__submit");
    private final SelenideElement successfulRegistrationMessage = $(".form__subheader");
    private final SelenideElement errorMessage = $("span.form__error");

    @Step("Check that page is loaded")
    @Override
    @Nonnull
    public RegisterPage checkThatPageLoaded() {
        usernameInput.should(visible);
        passwordInput.should(visible);
        confirmPassword.should(visible);
        return this;
    }

    @Step("Set username: {0}")
    @Nonnull
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    @Step("Set password: {0}")
    @Nonnull
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    @Step("Set confirm password: {0}")
    @Nonnull
    public RegisterPage setConfirmPassword(String password) {
        confirmPassword.setValue(password);
        return this;
    }

    @Step("Click sign up button")
    @Nonnull
    public RegisterPage clickSignUpButton() {
        signUpButton.click();
        return this;
    }

    @Step("Click sign in button")
    @Nonnull
    public LoginPage signIn() {
        signInLink.click();
        return new LoginPage();
    }

    @Step("Check message after successful registration: {0}")
    public void checkMessageThatRegistrationWasSuccessful(String text) {
        successfulRegistrationMessage.shouldHave(text(text)).shouldBe(visible);
    }

    @Step("Check message about username already exist: {0}")
    public void checkMessageThatUsernameAlreadyExist(String username) {
        String usernameAlreadyExistMessage = String.format("Username `%s` already exists", username);
        errorMessage.shouldHave(text(usernameAlreadyExistMessage)).shouldBe(visible);
    }

    @Step("Check message about password and confirmPassword not equals: {0}")
    public void checkMessageThatPasswordAndSubmitPasswordNotEquals(String text) {
        errorMessage.shouldHave(text(text)).shouldBe(visible);
    }
}
