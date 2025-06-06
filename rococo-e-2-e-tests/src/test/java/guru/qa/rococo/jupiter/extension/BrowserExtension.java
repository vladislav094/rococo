package guru.qa.rococo.jupiter.extension;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.Allure;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.io.ByteArrayInputStream;

public class BrowserExtension implements
        BeforeEachCallback,
        AfterEachCallback,
        TestExecutionExceptionHandler,
        LifecycleMethodExecutionExceptionHandler {

    static {
        String browser = System.getenv("BROWSER");
        if (browser == null || browser.isEmpty()) {
            browser = "chrome";
        }

        Configuration.timeout = 8000;
        Configuration.pageLoadStrategy = "eager";
//        Configuration.browserSize = "1920x1080";

        if ("docker".equals(System.getProperty("test.env"))) {
            Configuration.remote = "http://selenoid:4444/wd/hub";

            if ("chrome".equalsIgnoreCase(browser)) {
                Configuration.browser = "chrome";
                Configuration.browserVersion = "127.0";
                Configuration.browserCapabilities = new ChromeOptions().addArguments("--no-sandbox");
            } else if ("firefox".equalsIgnoreCase(browser)) {
                Configuration.browser = "firefox";
                Configuration.browserVersion = "125.0";
                Configuration.browserCapabilities = new FirefoxOptions();
            } else {
                throw new IllegalArgumentException("Unsupported browser: " + browser);
            }
        } else {
            Configuration.browser = "chrome";
            Configuration.browserVersion = "127.0";
            Configuration.browserCapabilities = new ChromeOptions().addArguments("--no-sandbox");
        }
    }

    public void beforeEach(ExtensionContext context) throws Exception {
        SelenideLogger.addListener("Allure-selenide", new AllureSelenide()
                .savePageSource(false)
                .screenshots(false)
        );
    }

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Selenide.closeWebDriver();
        }
    }

    @Override
    public void handleAfterEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        throw throwable;
    }

    @Override
    public void handleBeforeEachMethodExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenshot();
        throw throwable;
    }

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        doScreenshot();
        throw throwable;
    }

    private static void doScreenshot() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Allure.addAttachment(
                    "Screen on fail",
                    new ByteArrayInputStream(
                            ((TakesScreenshot) WebDriverRunner.getWebDriver()).getScreenshotAs(OutputType.BYTES))
            );
        }
    }
}
