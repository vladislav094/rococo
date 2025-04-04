package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.LoginPage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    private final SelenideElement mainPageLink = self.$("a[href*='/']");
    private final SelenideElement paintingPageLink = self.$("a[href*='/painting']");
    private final SelenideElement artistPageLink = self.$("a[href*='/artist']");
    private final SelenideElement museumPageLink = self.$("a[href*='/museum']");
    private final SelenideElement darkModeToggle = self.$("[role='switch']");
    private final SelenideElement loginButton = self.$(".btn.variant-filled-primary");
    private final SelenideElement profileButton = self.$("button.btn-icon.variant-filled-surface");

    private final SelenideElement avatarWithInitials = self.$(".avatar-initials");
    private final SelenideElement avatarWithImage = self.$(".avatar-image");

    public Header() {
        super($("#shell-header"));
    }

    public LoginPage toLoginPage() {
        loginButton.click();
        return new LoginPage();
    }

    @Step("Check that profile button is active and displayed after authorization")
    public Header checkThatProfileButtonIsActiveAndDisplayed() {
        profileButton.should(exist);
        profileButton.shouldBe(visible);
        profileButton.shouldNotBe(disabled);
        return this;
    }

    public ProfileModal toProfileModal() {
        profileButton.click();
        return new ProfileModal();
    }
}
