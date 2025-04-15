package guru.qa.rococo.page.modal;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class ArtistModal extends BaseModal<ArtistModal> {

    private final SelenideElement nameInput = $("input[name='name']");
    private final SelenideElement uploadPhotoButton = $("input[name='photo']");
    private final SelenideElement biographyTextarea = $("textarea[name='biography']");

    @Override
    @Step("Check that artist modal successful loaded")
    public ArtistModal checkThatModalLoaded() {
        modalContent.should(visible).shouldHave(
                text("Новый художник"),
                text("Заполните форму, чтобы добавить нового художника"));
        nameInput.shouldBe(visible);
        uploadPhotoButton.shouldBe(visible);
        biographyTextarea.shouldBe(visible);
        return this;
    }

    @Override
    @Step("Check that edit artist modal successful loaded")
    public ArtistModal checkThatEditModalLoaded() {
        modalContent.should(visible).shouldHave(
                text("Редактировать художника"),
                text("Обновить изображение художника")
        );
        nameInput.shouldBe(visible);
        uploadPhotoButton.shouldBe(visible);
        biographyTextarea.shouldBe(visible);
        return this;
    }

    @Step("Set artist name: {name}")
    @Nonnull
    public ArtistModal setName(String name) {
        nameInput.clear();
        nameInput.setValue(name);
        return this;
    }

    @Step("Set artist biography: {biography}")
    @Nonnull
    public ArtistModal setBiography(String biography) {
        biographyTextarea.clear();
        biographyTextarea.setValue(biography);
        return this;
    }

    @Step("Upload artist photo from file {0}")
    @Nonnull
    public ArtistModal uploadPhoto(String path) {
        uploadPhotoButton.uploadFromClasspath(path);
        return this;
    }
}
