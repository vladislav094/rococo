package guru.qa.rococo.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.Painting;
import guru.qa.rococo.jupiter.annotation.meta.WebTest;
import guru.qa.rococo.page.MuseumPage;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@WebTest
@Story("Управлением разделом картины")
public class PaintingWebTest extends BaseWebTest {


    @Painting
    @ApiLogin(username = "root", password = "1234")
    @Test
    @DisplayName("Поиск картины по названию в строке поиска")
    void testCheck() throws InterruptedException {


        Selenide.open(MuseumPage.URL, MuseumPage.class);
        Thread.sleep(1000);
    }
}
