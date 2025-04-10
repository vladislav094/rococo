package guru.qa.rococo.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.page.modal.MuseumModal;
import guru.qa.rococo.page.modal.PaintingModal;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;

public class PaintingPage extends BasePage<PaintingPage> {

    public static final String URL = CFG.frontUrl() + "painting";
    private final SelenideElement addPaintingButton = $(".btn.variant-filled-primary");
    private final SelenideElement editPaintingButton = $("button[data-testid='edit-painting']");
    private final ElementsCollection paintingItems = $$x("//ul[contains(@class, 'grid')]//li[.//a[contains(@href, '/painting/')]]");

    @Override
    public PaintingPage checkThatPageLoaded() {
        pageContent.should(visible).shouldHave(text("Картины"));
        return this;
    }

    @Step("Click on the add new painting button")
    public PaintingModal clickAddPaintingButton() {
        addPaintingButton.shouldBe(visible).shouldHave(text("Добавить картину"));
        addPaintingButton.click();
        return new PaintingModal();
    }

    @Step("Click on the edit painting button")
    public PaintingModal clickEditPaintingButton() {
        editPaintingButton.shouldBe(visible).shouldHave(text("Редактировать"));
        editPaintingButton.click();
        return new PaintingModal();
    }

    @Step("Go on painting page by title: {title}")
    public PaintingPage toPaintingCardByTitle(String title) {
        paintingItems.find(text(title)).click();
        return this;
    }

    @Step("Check all painting data on the page")
    public void checkMuseumCardData(MuseumJson museumJson) {
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
    }
}
