package guru.qa.rococo.page.modal;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class BaseModal<T extends BaseModal<?>> {

    protected final SelenideElement self;

    protected final SelenideElement submitButton = $(".btn.variant-filled-primary[type='submit']");

    public BaseModal() {
        this.self = $(".modal.contents");
    }

    @Nonnull
    public SelenideElement getSelf() {
        return self;
    }

    @Step("Click submit button")
    @Nonnull
    public T clickSubmitButton() {
        submitButton.click();
        return (T) this;
    }

    @Step("Scroll to element: {elementName}")
    public T scrollToElement(ElementsCollection selectCollection, String elementName) {

        int maxAttempts = 30;
        String lastElement = "";

        for (int attempt = 0; attempt < maxAttempts; attempt++) {

            SelenideElement target = selectCollection.findBy(text(elementName));
            if (target.exists() && target.isDisplayed()) {
                target.scrollIntoView("{block: 'center'}").click();
                return (T) this;
            }

            String currentLast = selectCollection.last().getText();
            if (currentLast.equals(lastElement)) {
                break;
            }
            lastElement = currentLast;

            selectCollection.last()
                    .scrollIntoView("{behavior: 'smooth', block: 'end'}");
            sleep(200);
        }

        throw new IllegalArgumentException("Element '" + elementName + "' was not found.");
    }
}
