package guru.qa.rococo.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.model.rest.PaintingJson;
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
        addPaintingButton.shouldBe(visible);
        searchInput.shouldBe(visible);
        searchButton.shouldBe(visible);
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

    @Step("Check painting data on the page")
    public PaintingPage checkPaintingCardData(PaintingJson paintingJson) {
        pageContent.shouldBe(visible)
                .shouldHave(
                        text(paintingJson.title()),
                        text(paintingJson.artist().name()),
                        text(paintingJson.description())
                );
        return this;
    }
}
