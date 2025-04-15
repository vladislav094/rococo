package guru.qa.rococo.jupiter.extension;

import io.qameta.allure.Allure;
import io.qameta.allure.AllureLifecycle;
import io.qameta.allure.model.TestResult;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class AllureBackendLogsExtension implements SuiteExtension {

    public static final String caseName = "Rococo backend logs";

    @SneakyThrows
    @Override
    public void afterSuite() {
        final AllureLifecycle allureLifecycle = Allure.getLifecycle();
        final String caseId = UUID.randomUUID().toString();
        allureLifecycle.scheduleTestCase(new TestResult().setUuid(caseId).setName(caseName));
        allureLifecycle.startTestCase(caseId);

        addLogAttachment("Rococo-auth log", "./logs/rococo-auth/app.log");
        addLogAttachment("Rococo-artist log", "./logs/rococo-artist/app.log");
        addLogAttachment("Rococo-gateway log", "./logs/rococo-gateway/app.log");
        addLogAttachment("Rococo-museum log", "./logs/rococo-museum/app.log");
        addLogAttachment("Rococo-painting log", "./logs/rococo-painting/app.log");
        addLogAttachment("Rococo-userdata log", "./logs/rococo-userdata/app.log");

        allureLifecycle.stopTestCase(caseId);
        allureLifecycle.writeTestCase(caseId);

    }

    @SneakyThrows
    private void addLogAttachment(String logName, String logPath) {
        Allure.getLifecycle().addAttachment(
                logName,
                "text/html",
                ".log",
                Files.newInputStream(Path.of(logPath))
        );
    }
}