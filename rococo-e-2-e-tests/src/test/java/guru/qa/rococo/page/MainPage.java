package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.component.Header;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl();

    protected final Header header = new Header();
    private final SelenideElement pageContent = $("#page-content");

    @Nonnull
    public Header getHeader() {
        return header;
    }

    @Step("Check that page is loaded")
    @Override
    @Nonnull
    public MainPage checkThatPageLoaded() {
        pageContent.should(visible).shouldHave(text("Ваши любимые картины и художники всегда рядом"));
        return this;
    }

    @Step("Check that user successful authorization")
    @Nonnull
    public MainPage checkSuccessfulLogin() {
        getHeader().checkThatProfileButtonIsActiveAndDisplayed();
        return this;
    }
}
