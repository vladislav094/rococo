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

import static guru.qa.rococo.utils.RandomDataUtils.*;

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
                .clickAddPaintingButton()
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
    @DisplayName("Создание новой картины")
    void testPaintingShouldChangedAfterEdit(PaintingJson paintingJson, ArtistJson artistJson, MuseumJson museumJson) {
        System.out.println(paintingJson.artist().name());
        System.out.println(paintingJson.museum().title());

        System.out.println(artistJson.name());
        System.out.println(museumJson.title());

        final String currentPaintingPage = String.format("%s/%s", PaintingPage.URL, paintingJson.id());
        final String randomPaintingTitle = RandomDataUtils.randomPaintingTitle();
        final String randomAuthorName = RandomDataUtils.randomArtistName();
        final String successfulUpdateMessage = String.format("Обновлен музей: %s", randomPaintingTitle);

        Selenide.open(currentPaintingPage, PaintingPage.class)
                .clickEditPaintingButton()
                .setTitle(randomPaintingTitle)
                .uploadPhoto(paintingPhotoPath);
//        page.museumPage.checkAlertMessage(successfulUpdateMessage);
    }
}
