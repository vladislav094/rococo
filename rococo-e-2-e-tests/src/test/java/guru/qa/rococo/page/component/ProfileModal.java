package guru.qa.rococo.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.rococo.page.MainPage;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selenide.$;

public class ProfileModal extends BaseComponent<ProfileModal>{

    private final SelenideElement nameInput = $("input[name='firstname']");

    public ProfileModal() {
        super($(".modal.contents"));
    }

    @Step("Set name: {0}")
    @Nonnull
    public MainPage setName(String name) {
        nameInput.clear();
        nameInput.setValue(name);
        return new MainPage();
    }
}
