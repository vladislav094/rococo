package guru.qa.rococo.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.page.modal.MuseumModal;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;

public class MuseumPage extends BasePage<MuseumPage> {

    public static final String URL = CFG.frontUrl() + "museum";
    private final SelenideElement addMuseumButton = $(".btn.variant-filled-primary");
    private final SelenideElement editMuseumButton = $("button[data-testid='edit-museum']");
    private final ElementsCollection museumItems = $$x("//ul[contains(@class, 'grid')]//li[.//a[contains(@href, '/museum/')]]");

    @Override
    @Step("Check that page has been loaded")
    public MuseumPage checkThatPageLoaded() {
        pageContent.should(visible).shouldHave(text("Музеи"));
        addMuseumButton.shouldBe(visible);
        searchInput.shouldBe(visible);
        searchButton.shouldBe(visible);
        return this;
    }

    @Step("Click on the add new museum button")
    public MuseumModal clickAddMuseumButton() {
        addMuseumButton.shouldBe(visible).shouldHave(text("Добавить музей"));
        addMuseumButton.click();
        return new MuseumModal();
    }

    @Step("Click on the edit museum button")
    public MuseumModal clickEditMuseumButton() {
        editMuseumButton.shouldBe(visible).shouldHave(text("Редактировать"));
        editMuseumButton.click();
        return new MuseumModal();
    }

    @Step("Go on museum page by title: {title}")
    public MuseumPage toMuseumCardByTitle(String title) {
        museumItems.find(text(title)).click();
        return this;
    }

    @Step("Check museum data on the page")
    public MuseumPage checkMuseumCardData(MuseumJson museumJson) {
        final String formattedGeo = String.format(
                "%s, %s",
                museumJson.geo().country().name(),
                museumJson.geo().city()
        );

        pageContent.shouldBe(visible)
                .shouldHave(
                        text(museumJson.title()),
                        text(formattedGeo),
                        text(museumJson.description())
                );
        return this;
    }
}
