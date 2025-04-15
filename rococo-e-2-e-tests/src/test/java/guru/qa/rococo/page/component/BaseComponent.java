package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;

import javax.annotation.Nonnull;

public class BaseComponent<T extends BaseComponent<?>> {

    protected final SelenideElement self;

    public BaseComponent(SelenideElement self) {
        this.self = self;
    }

    @Nonnull
    public SelenideElement getSelf() {
        return self;
    }
}
