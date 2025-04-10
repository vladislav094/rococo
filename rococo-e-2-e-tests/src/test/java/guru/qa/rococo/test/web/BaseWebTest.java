package guru.qa.rococo.test.web;

import guru.qa.rococo.config.Config;
import guru.qa.rococo.jupiter.extension.BrowserExtension;
import guru.qa.rococo.page.*;
import guru.qa.rococo.page.modal.ProfileModal;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class BaseWebTest {

    protected static final Config CFG = Config.getInstance();
    protected static final String frontUrl = CFG.frontUrl();
    protected final Page page = new Page();

    protected static class Page {
        protected final LoginPage loginPage = new LoginPage();
        protected final RegisterPage registerPage = new RegisterPage();
        protected final MainPage mainPage = new MainPage();
        protected final MuseumPage museumPage = new MuseumPage();
        protected final PaintingPage paintingPage = new PaintingPage();
    }
}
