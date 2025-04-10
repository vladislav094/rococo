package guru.qa.rococo.page.modal;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class PaintingModal extends BaseModal<PaintingModal> {

    private final SelenideElement titleInput = $("input[name='title']");
    private final SelenideElement uploadPhotoButton = $("input[name='content']");
    private final ElementsCollection authorIdSelect = $$("select[name='authorId'] option");
    private final SelenideElement descriptionTextarea = $("textarea[name='description']");
    private final ElementsCollection museumIdSelect = $$("select[name='museumId'] option");

    public PaintingModal() {
        super();
    }

    @Step("Set painting title: {title}")
    @Nonnull
    public PaintingModal setTitle(String title) {
        titleInput.clear();
        titleInput.setValue(title);
        return this;
    }

    @Step("Upload painting photo from file {0}")
    @Nonnull
    public PaintingModal uploadPhoto(String path) {
        uploadPhotoButton.uploadFromClasspath(path);
        return this;
    }

    @Step("Select museum with name: {museum}")
    public PaintingModal selectMuseum(String museum) {
        scrollToElement(museumIdSelect, museum);
        return this;
    }

    @Step("Select author with name: {author}")
    public PaintingModal selectAuthor(String author) {
        scrollToElement(authorIdSelect, author);
        return this;
    }

    @Step("Set painting description: {description}")
    @Nonnull
    public PaintingModal setDescription(String description) {
        descriptionTextarea.clear();
        descriptionTextarea.setValue(description);
        return this;
    }
}
