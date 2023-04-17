package tests.web;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import jupiter.annotation.meta.DBTest;
import org.junit.jupiter.api.BeforeEach;
import config.Config;
import jupiter.annotation.meta.WebTest;

@WebTest
@DBTest
public abstract class BaseWebTest {

    protected static final Config CFG = Config.getConfig();

    @BeforeEach
    void setup() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(false)
                .savePageSource(false)
        );
    }

}
