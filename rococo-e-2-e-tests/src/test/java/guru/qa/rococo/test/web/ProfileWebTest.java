package guru.qa.rococo.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.rococo.jupiter.annotation.ApiLogin;
import guru.qa.rococo.jupiter.annotation.User;
import guru.qa.rococo.jupiter.annotation.meta.WebTest;
import guru.qa.rococo.model.rest.UserJson;
import guru.qa.rococo.page.MainPage;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Страница профиля")
@WebTest
public class ProfileWebTest extends BaseWebTest {

    @User
    @ApiLogin
    @Test
    @Story("Успешная авторизация")
    @DisplayName("Выполняем авторизацию пользователя")
    public void testMainPageShouldBeDisplayedAfterSuccessfulLogin(UserJson user) {

        Selenide.open(MainPage.URL, MainPage.class)
                .getHeader()
                .toProfileModal()
                .setName("Serafim");
    }
}
