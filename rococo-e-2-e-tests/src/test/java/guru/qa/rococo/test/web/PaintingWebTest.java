package guru.qa.rococo.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.Artist;
import guru.qa.rococo.jupiter.annotation.Museum;
import guru.qa.rococo.jupiter.annotation.Painting;
import guru.qa.rococo.jupiter.annotation.meta.WebTest;
import guru.qa.rococo.model.rest.ArtistJson;
import guru.qa.rococo.model.rest.MuseumJson;
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


    @Painting(museum = @Museum(city = "Борисов"), artist = @Artist(name = "Левитан"))
    @ApiLogin(username = "root", password = "1234")
    @Test
    @DisplayName("Создание новой картины")
    void testCheck() throws InterruptedException {


        Selenide.open(MuseumPage.URL, MuseumPage.class);
        Thread.sleep(1000);
    }
}
