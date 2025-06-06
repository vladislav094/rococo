package guru.qa.rococo.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.utils.ScreenDiffResult;
import io.qameta.allure.Step;
import lombok.SneakyThrows;
import org.openqa.selenium.By;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl();
    private final SelenideElement avatar = $$("img[src^='data:image/png;base64']").get(0);

    private final SelenideElement paintingLinkOnMainContent =
            $(By.xpath("//a[.//img[@alt='Ссылка на картины'] and .//div[text()='Картины']]"));
    private final SelenideElement artistLinkOnMainContent =
            $(By.xpath("//a[.//img[@alt='Ссылка на художников'] and .//div[text()='Художники']]"));
    private final SelenideElement museumLinkOnMainContent =
            $(By.xpath("//a[.//img[@alt='Ссылка на музеи'] and .//div[text()='Музеи']]"));

    @Step("Check that page is loaded")
    @Override
    @Nonnull
    public MainPage checkThatPageLoaded() {
        pageContent.should(visible).shouldHave(text("Ваши любимые картины и художники всегда рядом"));
        paintingLinkOnMainContent.shouldBe(visible);
        artistLinkOnMainContent.shouldBe(visible);
        artistLinkOnMainContent.shouldBe(visible);
        return this;
    }

    @Step("Check that user successful authorization")
    @Nonnull
    public MainPage checkSuccessfulLogin() {
        getHeader().checkThatProfileButtonIsActiveAndDisplayed();
        return this;
    }

    @SneakyThrows
    @Step("Check avatar equals expected picture")
    public void checkThatAvatarEqualsUploadingImage(BufferedImage expected) {
        BufferedImage actual = ImageIO.read(avatar.screenshot());
        assertFalse(new ScreenDiffResult(actual, expected));
    }
}
