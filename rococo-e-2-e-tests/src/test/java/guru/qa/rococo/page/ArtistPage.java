package guru.qa.rococo.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.model.rest.PaintingJson;
import guru.qa.rococo.page.modal.ArtistModal;
import guru.qa.rococo.page.modal.PaintingModal;
import io.qameta.allure.Step;

import javax.annotation.Nullable;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;

public class ArtistPage extends BasePage<ArtistPage> {

    public static final String URL = CFG.frontUrl() + "artist";
    private final SelenideElement addButton = $(".btn.variant-filled-primary");
    private final SelenideElement editArtistButton = $("button[data-testid='edit-artist']");
    private final ElementsCollection artistItems = $$x("//ul[contains(@class, 'grid')]//li[.//a[contains(@href, '/artist/')]]");
    private final ElementsCollection paintingItems = $$x("//ul[contains(@class, 'grid')]//li[.//a[contains(@href, '/painting/')]]");

    @Override
    public ArtistPage checkThatPageLoaded() {
        pageContent.should(visible).shouldHave(text("Художники"));
        addButton.shouldBe(visible);
        searchInput.shouldBe(visible);
        searchButton.shouldBe(visible);
        return this;
    }

    @Step("Click on the add new artist button")
    public ArtistModal clickAddArtistButton() {
        addButton.shouldBe(visible).shouldHave(text("Добавить художника"));
        addButton.click();
        return new ArtistModal();
    }

    @Step("Click on the edit artist button")
    public ArtistModal clickEditArtistButton() {
        editArtistButton.shouldBe(visible).shouldHave(text("Редактировать"));
        editArtistButton.click();
        return new ArtistModal();
    }

    @Step("Click on the add new painting for current artist button")
    public PaintingModal clickAddPaintingArtist() {
        addButton.shouldBe(visible).shouldHave(text("Добавить картину"));
        addButton.click();
        return new PaintingModal();
    }

    @Step("Go on artist page by name: {name}")
    public ArtistPage toArtistCardByName(String name) {
        artistItems.find(text(name)).click();
        return this;
    }

    @Step("Check artist data on the page")
    public ArtistPage checkArtistCardData(ArtistJson artistJson, @Nullable List<PaintingJson> paintings) {
        pageContent.shouldBe(visible)
                .shouldHave(
                        text(artistJson.name()),
                        text(artistJson.biography())
                );
        return this;
    }
}
