package guru.qa.rococo.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.rococo.jupiter.annotation.*;
import guru.qa.rococo.jupiter.annotation.meta.WebTest;
import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.model.rest.MuseumJson;
import guru.qa.rococo.model.rest.PaintingJson;
import guru.qa.rococo.page.MuseumPage;
import guru.qa.rococo.page.PaintingPage;
import guru.qa.rococo.utils.RandomDataUtils;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static guru.qa.rococo.utils.RandomDataUtils.randomDescription;

@WebTest
@Story("Управлением разделом картины")
public class PaintingWebTest extends BaseWebTest {

    private static final String paintingPhotoPath = "img/ivan-terrible.jpg";

    @Artist
    @Museum
    @ApiLogin(username = "root", password = "1234")
    @Test
    @DisplayName("Создание новой картины")
    void testAddingNewPainting(ArtistJson artist, MuseumJson museum) {

        final String randomTitle = RandomDataUtils.randomPaintingTitle();
        final String successfulCreateMessage = String.format("Добавлена картина: %s", randomTitle);

        Selenide.open(PaintingPage.URL, PaintingPage.class)
                .checkThatPageLoaded()
                .clickAddPaintingButton()
                .checkThatModalLoaded()
                .setTitle(randomTitle)
                .uploadPhoto(paintingPhotoPath)
                .selectAuthor(artist.name())
                .setDescription(randomDescription())
                .selectMuseum(museum.title())
                .clickSubmitButton();
        page.paintingPage.checkAlertMessage(successfulCreateMessage);
    }

    @Artist
    @Museum
    @Painting
    @User
    @ApiLogin
    @Test
    @DisplayName("Редактирование картины")
    void testPaintingShouldChangedAfterEdit(PaintingJson paintingJson, ArtistJson artistJson, MuseumJson museumJson) {

        final String currentPaintingPage = String.format("%s/%s", PaintingPage.URL, paintingJson.id());
        final String randomPaintingTitle = RandomDataUtils.randomPaintingTitle();
        final String successfulUpdateMessage = String.format("Обновлена картина: %s", randomPaintingTitle);

        Selenide.open(currentPaintingPage, PaintingPage.class)
                .checkPaintingCardData(paintingJson)
                .clickEditPaintingButton()
                .checkThatEditModalLoaded()
                .setTitle(randomPaintingTitle)
                .uploadPhoto(paintingPhotoPath)
                .selectAuthor(artistJson.name())
                .setDescription(randomDescription())
                .selectMuseum(museumJson.title())
                .clickSubmitButton();
        page.paintingPage.checkAlertMessage(successfulUpdateMessage);
    }

    @Painting
    @User
    @ApiLogin
    @Test
    @DisplayName("Поиск картины по названию в строке поиска")
    void testPaintingShouldFindingByTitleInSearchField(PaintingJson paintingJson) {

        final String currentPaintingTitle = paintingJson.title();

        Selenide.open(PaintingPage.URL, PaintingPage.class)
                .checkThatPageLoaded()
                .fillSearchInput(currentPaintingTitle)
                .toPaintingCardByTitle(currentPaintingTitle)
                .checkPaintingCardData(paintingJson);
    }
}
