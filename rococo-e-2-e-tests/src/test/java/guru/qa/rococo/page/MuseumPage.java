package guru.qa.rococo.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.modal.MuseumModal;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MuseumPage extends BasePage<MuseumPage> {

    public static final String URL = CFG.frontUrl()  + "museum";
    private final SelenideElement addMuseumButton = $(".btn.variant-filled-primary");

    @Override
    public MuseumPage checkThatPageLoaded() {
        pageContent.should(visible).shouldHave(text("Музеи"));
        return this;
    }

    public MuseumModal clickAddMuseumButton() {
        addMuseumButton.shouldBe(visible).shouldHave(text("Добавить музей"));
        addMuseumButton.click();
        return new MuseumModal();
    }
}
