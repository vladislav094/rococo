package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.config.Config;
import guru.qa.rococo.page.component.Header;
import guru.qa.rococo.page.modal.ProfileModal;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public abstract class BasePage<T extends BasePage<?>> {

    protected static final Config CFG = Config.getInstance();

    protected final Header header = new Header();
    protected final ProfileModal profileModal = new ProfileModal();
    protected final SelenideElement alert = $("div[role='alertdialog']");
    protected final SelenideElement pageContent = $("#page-content");
    protected final SelenideElement searchInput = $("input[type='search']");
    protected final SelenideElement searchButton = $("button[class='btn-icon variant-soft-surface ml-4']");

    public abstract T checkThatPageLoaded();

    @Step("Check message: {0}")
    @Nonnull
    public T checkAlertMessage(String message) {
        alert.should(visible)
                .should(text(message));
        return (T) this;
    }

    @Nonnull
    public Header getHeader() {
        return header;
    }

    @Nonnull
    public ProfileModal getProfileModal() {
        return header.toProfileModal();
    }

    @Step("Fill input for searching: {name}")
    public T fillSearchInput(String name) {
        searchInput.clear();
        searchInput.setValue(name);
        searchButton.click();
        return (T) this;
    }
}
