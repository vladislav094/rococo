package guru.qa.rococo.page.modal;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.MainPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ProfileModal extends BaseModal<ProfileModal> {

    private final SelenideElement firstnameInput = $("input[name='firstname']");
    private final SelenideElement lastnameInput = $("input[name='surname']");
    private final SelenideElement closeModalButton = $("button.btn.variant-ringed[type='button']");
    private final SelenideElement logoutButton = $(".btn.variant-ghost[type='button']");
    private final SelenideElement uploadPictureButton = $("input[name='content']");


    @Override
    @Step("Check that profile modal successful loaded")
    public ProfileModal checkThatModalLoaded() {
        modalContent.should(visible).shouldHave(text("Профиль"));
        firstnameInput.shouldBe(visible);
        lastnameInput.shouldBe(visible);
        closeModalButton.shouldBe(visible);
        logoutButton.shouldBe(visible);
        uploadPictureButton.shouldBe(visible);
        return this;
    }

    @Override
    public ProfileModal checkThatEditModalLoaded() {
        return this;
    }

    @Step("Logout from profile")
    @Nonnull
    public MainPage logout() {
        logoutButton.click();
        return new MainPage();
    }

    @Step("Upload picture from file {0}")
    @Nonnull
    public ProfileModal uploadPicture(String path) {
        uploadPictureButton.uploadFromClasspath(path);
        return this;
    }

    @Step("Set firstname: {0}")
    @Nonnull
    public ProfileModal setFirstname(String firstname) {
        firstnameInput.clear();
        firstnameInput.setValue(firstname);
        return this;
    }

    @Step("Set lastname: {0}")
    @Nonnull
    public ProfileModal setLastname(String lastname) {
        lastnameInput.clear();
        lastnameInput.setValue(lastname);
        return this;
    }

    @Step("Close profile modal")
    @Nonnull
    public MainPage closeModal() {
        closeModalButton.click();
        return new MainPage();
    }
}
