package guru.qa.rococo.page.modal;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class MuseumModal extends BaseModal<MuseumModal> {

    private final SelenideElement titleInput = $("input[name='title']");
    private final SelenideElement cityInput = $("input[name='city']");
    private final SelenideElement uploadPhotoButton = $("input[name='photo']");
    private final SelenideElement descriptionTextarea = $("textarea[name='description']");
    private final ElementsCollection countryIdSelect = $$("select[name='countryId'] option");

    public MuseumModal() {
        super();
    }

    @Step("Set museum title: {title}")
    @Nonnull
    public MuseumModal setTitle(String title) {
        titleInput.clear();
        titleInput.setValue(title);
        return this;
    }

    @Step("Set museum city: {city}")
    @Nonnull
    public MuseumModal setCity(String city) {
        cityInput.clear();
        cityInput.setValue(city);
        return this;
    }

    @Step("Upload museum photo from file {0}")
    @Nonnull
    public MuseumModal uploadPhoto(String path) {
        uploadPhotoButton.uploadFromClasspath(path);
        return this;
    }

    @Step("Select country with name: {country}")
    public MuseumModal selectCountry(String country) {
        scrollToElement(countryIdSelect, country);
        return this;
    }

    @Step("Set museum description: {description}")
    @Nonnull
    public MuseumModal setDescription(String description) {
        descriptionTextarea.clear();
        descriptionTextarea.setValue(description);
        return this;
    }
}
