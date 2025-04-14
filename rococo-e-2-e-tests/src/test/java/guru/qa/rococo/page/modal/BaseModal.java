package guru.qa.rococo.page.modal;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.openqa.selenium.Keys;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public abstract class BaseModal<T extends BaseModal<?>> {

    protected final SelenideElement modalContent = $(".modal.contents");

    protected final SelenideElement submitButton = $(".btn.variant-filled-primary[type='submit']");
    protected final SelenideElement closeButton = $(".btn.variant-ringed[type='button']");

    public abstract T checkThatModalLoaded();

    public abstract T checkThatEditModalLoaded();

    @Step("Click submit button")
    @Nonnull
    public T clickSubmitButton() {
        submitButton.click();
        return (T) this;
    }

    @Step("Scroll to element: {elementName}")
    public T scrollToElement(SelenideElement selectElement, String elementName) {

//        SelenideElement selectElement = $("select.select");
        ElementsCollection selectElementOption = $$("select.select option");

        selectElement.click(); // Открываем список
        while (selectElementOption.filterBy(Condition.text(elementName)).isEmpty()) {
            selectElement.sendKeys(Keys.PAGE_DOWN);
            sleep(100);
        }
        selectElementOption.findBy(text(elementName)).click();

        return (T) this;
    }
}
